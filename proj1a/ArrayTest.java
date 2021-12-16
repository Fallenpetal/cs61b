public class ArrayTest {
    public static void main(String[] args){
        ArrayDeque<Integer> ArrayDeque = new ArrayDeque<>();
        ArrayDeque.addFirst(0);
        ArrayDeque.addFirst(1);
        ArrayDeque.addFirst(2);
        ArrayDeque.removeLast();
        ArrayDeque.addFirst(4);
        ArrayDeque.addFirst(5);
        ArrayDeque.removeLast();
    }
}
