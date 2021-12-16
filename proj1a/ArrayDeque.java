public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int front;
    private int back;
    private int length;

    public ArrayDeque() {
        items = (T[])new Object[8];
        size = 0;
        front = 7;
        back = 7;
        length = items.length;
    }

    public boolean isEmpty() {
        if (size == 0) 
            return true;
        else 
            return false;
        }
    
    private void resize(int capacity) {
        T[] a = (T[])new Object[capacity];
        if (back > front) {
            System.arraycopy(items, 0, a, 0, length);
        } else {
            System.arraycopy(items, 0, a, 0, back + 1);
            System.arraycopy(
                items,
                front,
                a,
                a.length - (items.length - front),
                items.length - front
            );
            front = a.length - (items.length - front);
        }
        items = a;
        length = items.length;
    }

    public void addFirst(T data) {
        if ((back + 1) % length == front) {
            resize(size * 2);
        }
        if (front == 0) {
            items[front] = data;
            front = length - 1;
        } else {
            items[front--] = data;
        }
        size = size + 1;
    }

    public void addLast(T data) {
        if ((back + 1) % length == front) {
            resize(size * 2);
        }
        back = (back + 1) % length;
        items[back] = data;
        size = size + 1;
    }

    public int size() {
        return size;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T temp = items[back];
        items[back] = null;
        if (back == 0) {
            back = length - 1;
        } else {
            back = (back - 1) % length;
        }
        size = size - 1;
        return (T)temp;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        front = (front + 1) % length;
        T temp = items[front];
        items[front] = null;
        size = size - 1;
        return (T)temp;
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        return items[index];
    }

    public void printDeque() {
        for (int i = (front + 1) % length; i % length != front; i = (i + 1) % length) {
            System
                .out
                .print(items[i] + " ");
        }
    }
}