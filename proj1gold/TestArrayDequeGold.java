import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    StudentArrayDeque<Integer> p1 = new StudentArrayDeque<>();
    ArrayDequeSolution<Integer> p2 = new ArrayDequeSolution<>();


    @Test
    public void testSolution1() {
        Integer actual;
        Integer expect;
        String message = "\n";
        for (int i = 0; i < 1000; i++) {
            double number = StdRandom.uniform();
//            System.out.println("第"+i+"次循环"+number);
            if (number < 0.25) {
                p1.addFirst(i);
                p2.addFirst(i);
                message = message + "addFirst(" + i + ")\n";
//                System.out.println(message);
                continue;
            }
            if (number < 0.5) {
                p1.addLast(i);
                p2.addLast(i);
                message = message + "addLast(" + i + ")\n";
//                System.out.println(message);
                continue;
            }
            if (number < 0.75) {
                if (p2.size() == 0 || p1.size() == 0) {
                    continue;
                }
                actual = p1.removeFirst();
                expect = p2.removeFirst();

                message = message + "removeFirst()\n";
                assertEquals(message, expect, actual);
//                System.out.println(message);
                continue;
            }
            if (number < 1) {
                if (p2.size() == 0 || p1.size() == 0) {
                    continue;
                }
                actual = p1.removeLast();
                expect = p2.removeLast();
                message = message + "removeLast()\n";
                assertEquals(message, expect, actual);
//                System.out.println(message);
                continue;
            }
        }
    }


}
