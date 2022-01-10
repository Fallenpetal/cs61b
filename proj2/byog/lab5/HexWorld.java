package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final Random RANDOM = new Random();

    public static int widthOfRowI(int size,int i) {
        int widthOfRowI = 0;
        int maxOfRow = size + 2*(size - 1);
        if (i < size) {
            widthOfRowI = size + 2*i;
        } else {
            widthOfRowI = maxOfRow - 2*(i - size);
        }
        return widthOfRowI;
    }

    public static int hexColumnHeight(int s, int i) {
        if (i >= s - 1) {
            i = 2 * (s - 1) - i;    //if s = 3, i = 4 - i     such as 3 4 5 4 3,the same value's index's sum is 4
        }
        return i;
    }

    public static int hexRowOffset(int size,int i) {
        if (i >= size) {
            i = 2 * size - 1 - i;
        }
        //total 2 * size - 1 rows,and 2 * size - 1 - i = i(the x offset)
        return - i;
    }

    public static int hexColumnOffset(int size, int i, int smallHexSize) {
        if (i >= size - 1) {
            i = 2 * (size - 1) - i;
        }

        return - i * smallHexSize;
    }

    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi++) {
            int x = p.x + xi;
            int y = p.y;
            world[x][y] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    public static void addHexagon(TETile[][] world, Position p, int size, TETile t, int x, int y) {
        if (size < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }
        p.x = x;
        p.y = y;
        for (int yi = 0; yi < 2 * size; yi++) {
            int thisRowY = p.y + yi;
            int xRowStart = p.x + hexRowOffset(size, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);
            int rowWidth = widthOfRowI(size, yi);
            addRow(world, rowStartP, rowWidth, t);
        }
    }

    public static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.SAND;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.TREE;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    public static void drawVerticalHexes(TETile[][] world, Position p, int N, int xStart, int yStart, int size) {
        for (int i = 0; i < N; i++) {
            TETile t = randomTile();
            addHexagon(world, p, size, t, xStart, yStart);
            yStart = yStart + 2 * size;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Position p = new Position();
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        // initialize tiles
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int N = 0;         //the param N is the number of Hex of every column.
        int bigHexSize = 3;
        int smallHexsize = 3;
        int smallHexXstart = smallHexsize - 1;    //column is 3, 4, 5, 4, 3
        int bigHexYstart = 10;
        int bigHexY = 0;
        for (int i = 0; i < 2 * bigHexSize - 1; i++) {
            N = bigHexSize + hexColumnHeight(bigHexSize, i);
            bigHexY = bigHexYstart + hexColumnOffset(bigHexSize, i, smallHexsize);
            drawVerticalHexes(world, p, N, smallHexXstart, bigHexY, bigHexSize);
            smallHexXstart = smallHexXstart + 2 * smallHexsize - 1;
        }

        ter.renderFrame(world);
    }


    @Test
    public void testHexRowWidth() {
        assertEquals(3, widthOfRowI(3, 5));
        assertEquals(5, widthOfRowI(3, 4));
        assertEquals(7, widthOfRowI(3, 3));
        assertEquals(7, widthOfRowI(3, 2));
        assertEquals(5, widthOfRowI(3, 1));
        assertEquals(3, widthOfRowI(3, 0));
        assertEquals(2, widthOfRowI(2, 0));
        assertEquals(4, widthOfRowI(2, 1));
        assertEquals(4, widthOfRowI(2, 2));
        assertEquals(2, widthOfRowI(2, 3));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }
}





