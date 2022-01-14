package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 89;
    public static final int HEIGHT = 45;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void mainMenu() {
        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 0.75, "CS61B:  THE GAME");
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 0.5, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 0.45, "Load Game (L)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 0.4, "Quit (Q)");
        StdDraw.show();
        StdDraw.pause(100);
    }

    public String solicitNCharsInput() {
        String input = "";
        drawFrame(input, WIDTH / 2, HEIGHT / 2);

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {          //如果没有检测到键盘输入，则一直循环
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'S') {
                break;
            }
            input += String.valueOf(key);
            drawFrame(input, WIDTH / 2, HEIGHT / 2);
        }
        StdDraw.pause(500);
        return input;
    }

    public void drawFrame(String s, double midWidth, double midHeight) {

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

    public MapGenerator loadGame() {
        File f = new File("./world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                MapGenerator loadGame = (MapGenerator) os.readObject();
                os.close();
                return loadGame;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }

    public void saveGame(MapGenerator map) {
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(map);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }


    public long inputNewSeed() {
        String s = "Please input a String as seed,then press S to be end.";
        drawFrame(s, WIDTH / 2, HEIGHT / 2);
        StdDraw.pause(2000);
        String seedString = solicitNCharsInput();
        return Long.parseLong(seedString);
    }

    public void congratulations() {
        String s = "Congratulations! You Win";
        drawFrame(s, WIDTH / 2, HEIGHT / 2);
    }

    public void playerMoveCase(MapGenerator map, int xStart, int yStart, int dx, int dy) {
        if (map.world[xStart + dx][yStart + dy].equals(Tileset.LOCKED_DOOR)) {
            congratulations();
            StdDraw.pause(2000);
            System.exit(0);
        }
        if (!map.world[xStart + dx][yStart + dy].equals(Tileset.WALL)) {
            map.world[xStart][yStart] = Tileset.FLOOR;
            map.world[xStart + dx][yStart + dy] = Tileset.PLAYER;
        }
        ter.renderFrame(map.world);
    }

    public void controlPlayerMove(MapGenerator map) {
        int x = map.playerPosition.x;
        int y = map.playerPosition.y;
//        headsUpDisplay(map);
        boolean saveAndExit = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'W' :
                        playerMoveCase(map, x, y, 0, 1);
                        if (!map.world[x][y + 1].equals(Tileset.WALL)) {
                            y = y + 1;
                        }
                        break;
                    case 'A' :
                        playerMoveCase(map, x, y, -1, 0);
                        if (!map.world[x - 1][y].equals(Tileset.WALL)) {
                            x = x - 1;
                        }
                        break;
                    case 'S' :
                        playerMoveCase(map, x, y, 0, -1);
                        if (!map.world[x][y - 1].equals(Tileset.WALL)) {
                            y = y - 1;
                        }
                        break;
                    case 'D' :
                        playerMoveCase(map, x, y, 1, 0);
                        if (!map.world[x + 1][y].equals(Tileset.WALL)) {
                            x = x + 1;
                        }
                        break;
                    case ':' :
                        saveAndExit = true;
                        break;
                    case 'Q' :
                        if (saveAndExit) {
                            saveGame(map);
                            System.exit(0);
                        }
                        break;
                    default:
                }
            }
        }
    }

    public void operationKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'N':
                        long seed = inputNewSeed();
                        MapGenerator map = new MapGenerator(seed);
                        map.returnWorld();
                        controlPlayerMove(map);
                        break;
                    case 'L':
                        MapGenerator loadGame = loadGame();
                        if (loadGame == null) {
                            System.exit(0);
                        }
                        loadGame.returnWorld();
                        controlPlayerMove(loadGame);
                        break;
                    case 'Q':
                        System.exit(0);
                        break;
                    default:
                }
            }
        }
    }

//    public void headsUpDisplay(MapGenerator map) {
//            int x = (int) StdDraw.mouseX();
//            int y = (int) StdDraw.mouseY();
//            System.out.println(x+","+y);
//            String s = "";
//            TETile[][] world = map.returnWorld();
//            if (world[x][y].equals(Tileset.WALL)) {
//                s = "Wall";
//                drawFrame(s, 0.9 * WIDTH, 0.1 * WIDTH);
//            }
//            if (world[x][y].equals(Tileset.FLOOR)) {
//                s = "Floor";
//                drawFrame(s, 0.9 * WIDTH, 0.1 * WIDTH);
//            }
//            if (world[x][y].equals(Tileset.PLAYER)) {
//                s = "Player";
//                drawFrame(s, 0.9 * WIDTH, 0.1 * WIDTH);
//            }
//            if (world[x][y].equals(Tileset.LOCKED_DOOR)) {
//                s = "Door";
//                drawFrame(s, 0.9 * WIDTH, 0.1 * WIDTH);
//            }
//    }


    public void playWithKeyboard() {
        mainMenu();
        operationKey();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        //  Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        String seedString = "";
        for (int i = 0; i < input.length(); i++) {
            char s = input.charAt(i);
            if (s  >= '0' && s <= '9') {
                seedString = seedString + s;
            }
        }
        long seed = Long.parseLong(seedString);
        MapGenerator map = new MapGenerator(seed);
        TETile[][] finalWorldFrame = map.returnTETileArray();
        return finalWorldFrame;
    }
}
