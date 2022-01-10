package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

import java.util.Random;

public class Room {
    protected int width;
    protected int height;
    protected int xStart;
    protected int yStart;
    private long SEED;
    protected Position leftBottom;
    protected Position rightBottom;
    protected Position rightTop;
    protected Position leftTop;

    public Room(int w, int h, Position p, long seedInput) {
        SEED = seedInput;
        width = w - 1;
        height = h - 1;                  //拐角单独画，因此减1
        xStart = p.x;
        yStart = p.y;
        leftBottom = new Position(xStart, yStart);
        rightBottom = new Position(xStart + width, yStart);
        rightTop = new Position(xStart + width, yStart + height);
        leftTop = new Position(xStart, yStart + height);
    }


    public Position randomMove(int flag) {
        Random gameRandom = new Random(SEED);
        int dx = 0;
        int dy = 0;
        Position p = new Position(this.xStart, this.yStart);
        switch (flag) {
            case 0 :
                dx = gameRandom.nextInt(width - 1) + 1;  //宽度已经比真实宽度小1 width = truly width - 1
                p.x = p.x + dx; break;        //随机地在Room的四条边界上移动,0为bottom,1为top,2为left,3为right
            case 1 :
                dx = gameRandom.nextInt(width - 1) + 1;
                p.x = p.x + dx;
                p.y = yStart + height; break;
            case 2 :
                dy = gameRandom.nextInt(height - 1) + 1;
                p.y = p.y + dy; break;
            case 3 :
                dy = gameRandom.nextInt(height - 1) + 1;
                p.y = p.y + dy;
                p.x = xStart + width; break;
            default:
                break;
        }
        return p;
    }

    public boolean isOverlap(Room it) {
        int rX = (it.width + this.width) / 2 + 1;            //house 1 与 house 2的x半径和
        int rY = (it.height + this.height) / 2 + 1;          //house 2 与 house 2的y半径和
        int oX1 = it.xStart + it.width / 2;
        int oY1 = it.yStart + it.height / 2;             //house it 的中心点x,y坐标
        int oX2 = this.xStart + this.width / 2;
        int oY2 = this.yStart + this.height / 2;          //house r 的中心点 x,y坐标
        if (Math.abs(oX2 - oX1) > rX || Math.abs(oY2 - oY1) > rY) {
            return false;            //这里要注意逻辑是 || 最开始我写的 &&,画图便知
        }
        return true;
    }

    public void drawVerticalWall(TETile[][] world, Position p, int heights, TETile type) {
        int x = p.x;
        int y = p.y;
        if (y + heights >= world[0].length) {
            heights = world[0].length - y - 1;
        }
        for (int i = 0; i < heights; i++) {
            y = y + 1;
            if (world[x][y] != Tileset.FLOOR) {               //不覆盖已有的地板
                world[x][y] = type;
            }
        }
    }

    public void drawHorizontalWall(TETile[][] world, Position p, int widths, TETile type) {
        int x = p.x;
        int y = p.y;
        if (x + widths >= world.length) {
            widths = world.length - x - 1;                       //超过边界则截断
        }
        for (int i = 0; i < widths; i++) {
            x = x + 1;
            if (world[x][y] != Tileset.FLOOR) {
                world[x][y] = type;
            }
        }
    }

    public void drawRoomFloor(TETile[][] world) {
        int x = xStart + 1;
        int y = yStart + 1;
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                world[x][y] = Tileset.FLOOR;
                x = x + 1;
            }
            x = xStart + 1;
            y = y + 1;
        }
    }


    public void drawRoom(TETile[][] world, TETile type) {
        world[xStart][yStart] = type;
        drawHorizontalWall(world, leftBottom, width, type);
        drawVerticalWall(world, leftBottom, height, type);
        drawRoomFloor(world);
        drawHorizontalWall(world, leftTop, width, type);
        drawVerticalWall(world, rightBottom, height, type);

    }

}
