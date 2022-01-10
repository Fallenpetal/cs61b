package byog.Core;

//import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {

    private static final int WIDTH = 89;
    private static final int HEIGHT = 45;
    private long SEED = 0;
    private static ArrayList<Room> existingRooms = new ArrayList<>();
    private static int[][] visited = new int[WIDTH][HEIGHT];        // 0表示未访问， 1表示访问了
    private static int[][] joinedList = new int[WIDTH][HEIGHT];     // 0表示未加入候选, 1表示已加入候选
    private static ArrayList<Point> candidateList = new ArrayList<>();
    private static TETile[][] world = new TETile[WIDTH][HEIGHT];

    public MapGenerator(long seedInput) {
        SEED = seedInput;
    }


    public boolean isOverlap(ArrayList<Room> L, Room r) {
        for (Room it : L) {
            if (r.isOverlap(it)) {
                return true;
            }
        }
        return false;
    }


    public void randomDrawRoom(int i) {
        Random gameRandom = new Random(SEED);
        while (i > 0) {
            int xStart = gameRandom.nextInt(WIDTH - 1) + 1;
            int yStart = gameRandom.nextInt(HEIGHT - 1) + 1;
            int width = gameRandom.nextInt(10) + 3;
            int height = gameRandom.nextInt(10) + 3;              //+1是为了防止产生0，导致数组越界
            Position p = new Position(xStart, yStart);
            Room r = new Room(width, height, p, SEED);
            if (!isOverlap(existingRooms, r)) {                //重叠判断
                if (xStart + width < world.length && yStart + height < world[0].length) {  //边界判断
                    existingRooms.add(r);
                    r.drawRoom(world, Tileset.WALL);
                }
            }
            i--;
        }
    }

    public void initializeRoom(ArrayList<Room> rooms) {
        for (var it : rooms) {
            for (int i = it.xStart; i <= it.rightBottom.x; i++) {
                for (int j = it.yStart; j <= it.leftTop.y; j++) {
                    visited[i][j] = 1;
                    joinedList[i][j] = 0;
                }
            }
        }
    }

    public void fillMap() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1) {
                    world[x][y] = Tileset.WALL;
                } else {
                    if (x % 2 == 0 || y % 2 == 0) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public Point firstSelectPoint() {
        Random gameRandom = new Random(SEED);
        int x = 0;
        int y = 0;
        Point p = new Point(0, 0);
        while (true) {
            x = gameRandom.nextInt(WIDTH);
            y = gameRandom.nextInt(HEIGHT);
            if (world[x][y].equals(Tileset.NOTHING)) {
                p.x = x;
                p.y = y;
                visited[p.x][p.y] = 1;
                joinedList[p.x][p.y] = 1;
                break;
            }
        }
        return p;
    }

    public void addCandidateList(Point p) {
        int x = 0;
        int y = 0;
        int[][] next = new int[][]{{0, 2}, {2, 0}, {0, -2}, {-2, 0}};         //上,右，下，左
        for (int i = 0; i < 4; i++) {
            x = p.x + next[i][0];
            y = p.y + next[i][1];
            if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
                continue;
            }
            if (visited[x][y] == 0 && joinedList[x][y] == 0) {
                Point candidatePoint = new Point(x, y);
                candidateList.add(candidatePoint);
                joinedList[x][y] = 1;
            }
        }
    }

    public void breakWall(Point p) {
        Random gameRandom = new Random(SEED);
        int x = 0;
        int y = 0;
        ArrayList<Point> aroundVisited = new ArrayList<>();
        int[][] next = new int[][]{{0, 2}, {2, 0}, {0, -2}, {-2, 0}};         //上,右，下，左
        for (int i = 0; i < 4; i++) {
            x = p.x + next[i][0];
            y = p.y + next[i][1];
            if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
                continue;
            }
            if (visited[x][y] == 1 && joinedList[x][y] == 1) {
                Point hasPass = new Point(x, y);
                aroundVisited.add(hasPass);
            }
        }
        int size = aroundVisited.size();
        int i = gameRandom.nextInt(size);
        Point chooseOnePoint = aroundVisited.get(i);
        if (Math.abs(chooseOnePoint.x - p.x) > 0) {                //该点在中心点左右
            if (chooseOnePoint.x - p.x > 0) {
                world[p.x + 1][p.y] = Tileset.FLOOR;            //该点在中心点右边
            } else {
                world[p.x - 1][p.y] = Tileset.FLOOR;
            }
        } else {                                                   //该点在中心点上下
            if (chooseOnePoint.y - p.y > 0) {
                world[p.x][p.y + 1] = Tileset.FLOOR;
            } else {
                world[p.x][p.y - 1] = Tileset.FLOOR;
            }
        }
        candidateList.remove(p);
    }

    public Point selectPointFromList() {
        Random gameRandom = new Random(SEED);
        int size = candidateList.size();
        int i = gameRandom.nextInt(size);
        Point centerPoint = candidateList.get(i);
        visited[centerPoint.x][centerPoint.y] = 1;
        addCandidateList(centerPoint);
        return centerPoint;
    }

    public void breakAllWall() {
        Point pfirst = firstSelectPoint();
        addCandidateList(pfirst);
        while (!candidateList.isEmpty()) {
            Point psecond = selectPointFromList();
            breakWall(psecond);
        }
    }


    public void randomBreakRoomWall(Room r) {
        int flag = 0;                  //随机地在Room的四条边界上移动,0为bottom,1为top,2为left,3为right
        Position p = r.randomMove(flag);
        int x = p.x;
        int y = p.y;
        while (true) {
            switch (flag) {    //这里我本来想随机在四条边上打路的，但是好像全部指定在上下两边打路，也能连通全图
                case 0 :
                    if (y - 1 <= 0) {
                        flag = 1;
                        continue;
                    }
                    world[x][y] = Tileset.FLOOR;
                    if (!world[x][y - 1].equals(Tileset.FLOOR)) {
                        y = y - 1;
                        continue;
                    }
                    break;
                case 1 :
                    if (y + 1 >= HEIGHT - 1) {
                        flag = 0;
                        continue;
                    }
                    world[x][y] = Tileset.FLOOR;
                    if (!world[x][y + 1].equals(Tileset.FLOOR)) {
                        y = y + 1;
                        continue;
                    }
                    break;
                default:
                    break;
            }
            break;
        }
    }

    public void breakAllRoom(ArrayList<Room> rooms) {
        for (var it : rooms) {
            randomBreakRoomWall(it);
        }
    }

    public void fillMapWithFloor() {
        for (int x = 1; x < WIDTH; x++) {
            for (int y = 1; y < HEIGHT; y++) {
                if (world[x][y].equals(Tileset.NOTHING)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public boolean isDeadEnds(int xStart, int yStart) {
        int x = 0;
        int y = 0;
        ArrayList<Point> deadEnds = new ArrayList<>();
        int[][] next = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};         //上,右，下，左
        for (int i = 0; i < 4; i++) {
            x = xStart + next[i][0];
            y = yStart + next[i][1];
            if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
                continue;
            }
            if (world[x][y].equals(Tileset.WALL)) {
                Point isWall = new Point(x, y);
                deadEnds.add(isWall);
            }
        }
        if (deadEnds.size() == 3) {
            return true;
        }
        return false;
    }

    public void fillAllEnds() {
        boolean completed = false;                //胡同有深度，一轮遍历只能填一格，又会形成一个小一点的胡同
        while (!completed) {                                   //因此多次遍历Map，直至填充完毕
            completed = true;
            for (int x = 1; x < WIDTH - 1; x++) {
                for (int y = 1; y < HEIGHT - 1; y++) {
                    if (world[x][y].equals(Tileset.FLOOR)) {
                        if (isDeadEnds(x, y)) {                  //只要还有胡同则说明未完成
                            world[x][y] = Tileset.WALL;
                            completed = false;
                        }
                    }
                }
            }
        }
    }


    public void destroyExtraWall() {
        int[][] next = new int[][]{{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.WALL)) {
                    boolean isDestroy = true;
                    for (int k = 0; k < 4; k++) {
                        int dx = i + next[k][0];
                        int dy = j + next[k][1];
                        if (dx < 0 || dx >= WIDTH || dy < 0 || dy >= HEIGHT) {
                            continue;
                        }
                        if (world[dx][dy].equals(Tileset.FLOOR)) {
                            isDestroy = false;       //判断某一点对角线上的四个点是否是FLOOR,只要有一个FLOOR即可
                        }
                    }
                    if (isDestroy) {
                        world[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
    }

    public void addGate() {
        Random gameRandom = new Random(SEED);
        while (true) {
            int x = gameRandom.nextInt(WIDTH);
            int y = gameRandom.nextInt(HEIGHT);
            if (world[x][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.LOCKED_DOOR;
                break;
            }
        }
    }

    public void initializeWorld() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    
    public TETile[][] returnWorld() {
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
        initializeWorld();
        fillMap();
        randomDrawRoom(100);
        initializeRoom(existingRooms);
        breakAllWall();
        breakAllRoom(existingRooms);
        fillMapWithFloor();
        fillAllEnds();
        destroyExtraWall();
        addGate();
//        ter.renderFrame(world);
        return world;
    }

//    public static void main(String[] args) {
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        initializeWorld();
//        fillMap();
//        randomDrawRoom(100);
//        initializeRoom(existingRooms);
//        breakAllWall();
//        breakAllRoom(existingRooms);
//        fillMapWithFloor();
//        fillAllEnds();
//        destroyExtralWall();
//        addGate();
//        ter.renderFrame(world);
//
//    }
}
