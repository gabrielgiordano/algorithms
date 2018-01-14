/*
 * Requirement for memory complexity:
 * A deque containing n items must use at most 48n + 192 bytes of memory
 * and use space proportional to the number of items currently in the deque.
 *
 * Requirement for time complexity:
 * Your deque implementation must support each deque operation
 * (including construction) in **constant** worst-case time
 * Additionally, your iterator implementation must support each operation
 * (including construction) in **constant** worst-case time.
*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> { // 16 bytes overhead
    private class Node<Item> { // 16 bytes overhead + 8 bytes extra overhead
        private Item item; // 8 bytes
        private Node<Item> next; // 8 bytes
        private Node<Item> previous; // 8 bytes
    }

    private Node<Item> first; // 8 bytes
    private Node<Item> last; // 8 bytes
    private int size; // 4 bytes

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0 ? true : false;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node<Item> node = new Node<>();

        node.item = item;
        node.next = first;
        node.previous = null;

        if (first != null) {
            first.previous = node;
            first = node;
        } else {
            first = node;
            last = node;
        }

        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node<Item> node = new Node<>();

        node.item = item;
        node.next = null;
        node.previous = last;

        if (last != null) {
            last.next = node;
            last = node;
        } else {
            first = node;
            last = node;
        }

        size++;
    }

    // remove and return the item from the end
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = first.item;

        first = first.next;

        if (first != null)
            first.previous = null;
        else
            last = null;

        size--;

        return item;
    }

    // remove and return the item from the front
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = last.item;

        last = last.previous;

        if (last != null)
            last.next = null;
        else
            first = null;

        size--;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        String[] inputs = StdIn.readAllStrings();

        for (String s : inputs) {
            deque.addFirst(s);
            deque.addLast(s);
        }

        for (int i = 0; i < 2 * inputs.length; i++)
            StdOut.println(deque.removeLast());

        StdOut.printf("\n");

        for (String s : deque)
            StdOut.println(s);
    }
}
