public class LinkedListDeque<Item> implements Deque<Item> {

    private class LinkNode<Item> {
        private Item data;
        private LinkNode prior;
        private LinkNode next;

        LinkNode(Item value, LinkNode prevPointer, LinkNode nextPointer) {
            data = value;
            prior = prevPointer;
            next = nextPointer;
        }

        LinkNode(Item value) {
            data = value;
        }
    }

    private LinkNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new LinkNode<>(999);
        sentinel.prior = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(Item item) {
        LinkNode p = new LinkNode<>(item);
        p.next = sentinel.next;
        p.prior = sentinel;
        sentinel.next.prior = p;
        sentinel.next = p;
        size = size + 1;
    }

    @Override
    public void addLast(Item item) {
        LinkNode p = new LinkNode<>(item);
        p.next = sentinel.prior.next;
        p.prior = sentinel.prior;
        sentinel.prior.next = p;
        sentinel.prior = p;
        size = size + 1;
    }

    public boolean isEmpty() {
        return  (size == 0);
    }
    
    public int size() {
        return size;
    }

    public void printDeque() {
        LinkNode p = sentinel.next;
        while (p != sentinel) {
            System
                .out
                .print(p.data + " ");
            p = p.next;
        }
    }

    @Override
    public Item removeFirst() {
        LinkNode p = sentinel.next;
        if (p == sentinel) {
            return null;
        }
        Item temp = (Item) p.data;
        p.next.prior = sentinel;
        sentinel.next = p.next;
        size = size - 1;
        return temp;
    }

    public Item removeLast() {
        LinkNode p = sentinel.prior;
        if (p == sentinel) {
            return null;
        }
        Item temp = (Item) p.data;
        p.prior.next = sentinel;
        sentinel.prior = p.prior;
        size = size - 1;
        return temp;
    }

    public Item get(int index) {
        LinkNode p = sentinel.next;
        int flag = 0;
        while (p != sentinel) {
            if (flag == index) {
                return (Item) p.data;
            }
            flag++;
            p = p.next;
        }
        return null;

    }

    /**Actrully getRecursive must add a new method,
   because the parameters are not enough,
   you can't compelete it by only one parameter which is index*/
    private Item getRecursiveExpend(LinkNode p, int index) {
        if (index == 0) {
            return (Item) p.data;
        }
        return getRecursiveExpend(p.next, index - 1);

    }

    public Item getRecursive(int index) {
        if (index > size) {
            return null;
        }
        return getRecursiveExpend(sentinel.next, index);
    }

}
