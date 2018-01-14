/*
 * Requirement for memory complexity:
 * A randomized queue containing n items must use
 * at most 48n + 192 bytes of memory.
 *
 * Requirement for time complexity:
 * Your randomized queue implementation must support each
 * randomized queue operation (besides creating an iterator)
 * in constant amortized time.
 * That is, any sequence of m randomized queue operations
 * (starting from an empty queue)
 * must take at most cm steps in the worst case, for some constant c.
 *
 * Requirement for iterator:
 * Each iterator must return the items in uniformly random order.
 * The order of two or more iterators to the same randomized queue must be
 * mutually independent; each iterator must maintain its own random order.
 *
 * Your iterator implementation must support operations next() and hasNext()
 * in constant worst-case time;
 * and construction in linear time;
 * you may (and will need to) use a linear amount of extra memory per iterator.
 */

import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int next;

    // construct an empty randomized queue
    public RandomizedQueue() {
        next = 0;
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return next == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return next;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (size() == items.length) resize(2 * items.length);

        items[next++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int random = randomIndex();
        Item item = items[random];

        items[random] = items[--next];
        items[next] = null;

        if (next > 0 && size() == items.length / 4) resize(items.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return items[randomIndex()];
    }

    private int randomIndex() {
        return StdRandom.uniform(next);
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];

        for (int i = 0; i < next; i++)
            copy[i] = items[i];

        items = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] randomItems;
        private int current;

        public RandomizedQueueIterator() {
            randomItems = (Item[]) new Object[next];

            for (int i = 0; i < next; i++)
                randomItems[i] = items[i];

            StdRandom.shuffle(randomItems);
            current = 0;
        }

        public boolean hasNext() {
            return current < randomItems.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            return randomItems[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(34);
        rq.dequeue();
        rq.enqueue(458);
    }
}
