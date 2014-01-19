package prefuse.util.collections;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/** Abstract base class for red-black trees that map a key value to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractTreeMap implements IntSortedMap {
    /** The Constant RED. */
    protected static final boolean RED = false;
    /** The Constant BLACK. */
    protected static final boolean BLACK = true;
    /** The Constant NIL. */
    protected static final Entry NIL = new Entry(Integer.MIN_VALUE);
    static {
        NIL.left = NIL.right = NIL.p = NIL;
    }
    /** The cmp. */
    protected LiteralComparator cmp = null;
    /** The root. */
    protected Entry root = NIL;
    /** The allow duplicates. */
    protected boolean allowDuplicates;
    /** The size. */
    protected int size = 0;
    /** The unique. */
    protected int unique = 0;
    /** The mod count. */
    protected int modCount = 0;
    /** The last order. */
    protected int lastOrder = 0;

    // ------------------------------------------------------------------------
    // Constructors
    /** Instantiates a new abstract tree map.
     * 
     * @param comparator
     *            the comparator
     * @param allowDuplicates
     *            the allow duplicates */
    public AbstractTreeMap(LiteralComparator comparator, boolean allowDuplicates) {
        cmp = comparator == null ? DefaultLiteralComparator.getInstance() : comparator;
        this.allowDuplicates = allowDuplicates;
    }

    // ------------------------------------------------------------------------
    // Accessor Methods
    public boolean isAllowDuplicates() {
        return allowDuplicates;
    }

    /** Size.
     * 
     * @return the int
     * @see java.util.Map#size() */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == NIL;
    }

    /** Comparator.
     * 
     * @return the comparator
     * @see java.util.SortedMap#comparator() */
    public Comparator comparator() {
        return cmp;
    }

    // ------------------------------------------------------------------------
    // SortedMap Methods
    /** Clear.
     * 
     * @see java.util.Map#clear() */
    public void clear() {
        ++modCount;
        size = 0;
        root = NIL;
    }

    public int getMinimum() {
        return minimum(root).getValue();
    }

    public int getMaximum() {
        return maximum(root).getValue();
    }

    public int getMedian() {
        Entry e = minimum(root);
        for (int i = 0; i < size / 2; ++i, e = successor(e)) {
            ;
        }
        return e.getValue();
    }

    public int getUniqueCount() {
        return unique;
    }

    /** Contains value.
     * 
     * @param value
     *            the value
     * @return true, if successful
     * @see java.util.Map#containsValue(java.lang.Object) */
    public boolean containsValue(int value) {
        return root == NIL ? false : containsValue(root, value);
    }

    /** Contains value.
     * 
     * @param e
     *            the e
     * @param value
     *            the value
     * @return true, if successful */
    private boolean containsValue(Entry e, int value) {
        if (e.val == value) {
            return true;
        } else {
            return e.left != NIL && containsValue(e.left, value) || e.right != NIL
                    && containsValue(e.right, value);
        }
    }

    // -- Collection view methods ---------------------------------------------
    public IntIterator valueIterator(boolean ascend) {
        return new ValueIterator(new EntryIterator(!ascend));
    }

    // ------------------------------------------------------------------------
    // Internal update methods
    /** Increment size.
     * 
     * @param isUnique
     *            the is unique */
    protected void incrementSize(boolean isUnique) {
        ++size;
        ++modCount;
        if (isUnique) {
            ++unique;
        }
    }

    /** Decrement size.
     * 
     * @param isUnique
     *            the is unique */
    protected void decrementSize(boolean isUnique) {
        --size;
        ++modCount;
        if (isUnique) {
            --unique;
        }
    }

    // ------------------------------------------------------------------------
    // Internal Binary Search Tree / Red-Black Tree methods
    // Adapted from Cormen, Leiserson, and Rivest's Introduction to Algorithms
    /** Compare.
     * 
     * @param e1
     *            the e1
     * @param e2
     *            the e2
     * @return the int */
    protected abstract int compare(Entry e1, Entry e2);

    /** Find.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry find(Entry x) {
        Entry y = root;
        while (y != NIL) {
            int cmp = compare(x, y);
            if (cmp == 0) {
                return y;
            } else if (cmp < 0) {
                y = y.left;
            } else {
                y = y.right;
            }
        }
        return y;
    }

    /** Find predecessor.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry findPredecessor(Entry x) {
        Entry y = root;
        while (y != NIL) {
            int cmp = compare(x, y);
            if (cmp > 0) {
                if (y.right == NIL) {
                    return y;
                }
                y = y.right;
            } else {
                if (y.left != NIL) {
                    y = y.left;
                } else {
                    Entry up = y.p, c = y;
                    for (; up != NIL && c == up.left; c = up, up = up.p) {
                        ;
                    }
                    return up;
                }
            }
        }
        return y;
    }

    /** Find ceiling.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry findCeiling(Entry x) {
        Entry y = root;
        while (y != NIL) {
            int cmp = compare(x, y);
            if (cmp == 0) {
                return y;
            } else if (cmp < 0) {
                if (y.left != NIL) {
                    y = y.left;
                } else {
                    return y;
                }
            } else {
                if (y.right != NIL) {
                    y = y.right;
                } else {
                    Entry up = y.p, c = y;
                    for (; up != NIL && c == up.right; c = up, up = up.p) {
                        ;
                    }
                    return up;
                }
            }
        }
        return y;
    }

    /** Minimum.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry minimum(Entry x) {
        for (; x.left != NIL; x = x.left) {
            ;
        }
        return x;
    }

    /** Maximum.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry maximum(Entry x) {
        for (; x.right != NIL; x = x.right) {
            ;
        }
        return x;
    }

    /** Successor.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry successor(Entry x) {
        // easy case - just traverse to the right
        if (x.right != NIL) {
            return minimum(x.right);
        }
        // else have to climb up
        Entry y = x.p;
        while (y != NIL && x == y.right) {
            x = y;
            y = y.p;
        }
        return y;
    }

    /** Predecessor.
     * 
     * @param x
     *            the x
     * @return the entry */
    protected Entry predecessor(Entry x) {
        // easy case - just traverse to the left
        if (x.left != NIL) {
            return maximum(x.left);
        }
        // else have to climb up
        Entry y = x.p;
        while (y != NIL && x == y.left) {
            x = y;
            y = y.p;
        }
        return y;
    }

    /** Rotate left.
     * 
     * @param x
     *            the x */
    protected void rotateLeft(Entry x) {
        Entry y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.p = x;
        }
        y.p = x.p;
        if (x.p == NIL) {
            root = y;
        } else if (x.p.left == x) {
            x.p.left = y;
        } else {
            x.p.right = y;
        }
        y.left = x;
        x.p = y;
    }

    /** Rotate right.
     * 
     * @param x
     *            the x */
    protected void rotateRight(Entry x) {
        Entry y = x.left;
        x.left = y.right;
        if (y.right != NIL) {
            y.right.p = x;
        }
        y.p = x.p;
        if (x.p == NIL) {
            root = y;
        } else if (x.p.right == x) {
            x.p.right = y;
        } else {
            x.p.left = y;
        }
        y.right = x;
        x.p = y;
    }

    /** Fix up insert.
     * 
     * @param x
     *            the x */
    protected void fixUpInsert(Entry x) {
        x.color = RED;
        while (x != NIL && x != root && x.p.color == RED) {
            if (x.p == x.p.p.left) {
                Entry y = x.p.p.right;
                if (y.color == RED) {
                    x.p.color = BLACK;
                    y.color = BLACK;
                    x.p.p.color = RED;
                    x = x.p.p;
                } else {
                    if (x == x.p.right) {
                        x = x.p;
                        rotateLeft(x);
                    }
                    x.p.color = BLACK;
                    x.p.p.color = RED;
                    if (x.p.p != NIL) {
                        rotateRight(x.p.p);
                    }
                }
            } else {
                // mirror image case
                Entry y = x.p.p.left;
                if (y.color == RED) {
                    x.p.color = BLACK;
                    y.color = BLACK;
                    x.p.p.color = RED;
                    x = x.p.p;
                } else {
                    if (x == x.p.left) {
                        x = x.p;
                        rotateRight(x);
                    }
                    x.p.color = BLACK;
                    x.p.p.color = RED;
                    if (x.p.p != NIL) {
                        rotateLeft(x.p.p);
                    }
                }
            }
        }
        root.color = BLACK;
    }

    /** Fix up remove.
     * 
     * @param x
     *            the x */
    protected void fixUpRemove(Entry x) {
        while (x != root && x.color == BLACK) {
            if (x == x.p.left) {
                Entry sib = x.p.right;
                if (sib.color == RED) {
                    sib.color = BLACK;
                    x.p.color = RED;
                    rotateLeft(x.p);
                    sib = x.p.right;
                }
                if (sib.left.color == BLACK && sib.right.color == BLACK) {
                    sib.color = RED;
                    x = x.p;
                } else {
                    if (sib.right.color == BLACK) {
                        sib.left.color = BLACK;
                        sib.color = RED;
                        rotateRight(sib);
                        sib = x.p.right;
                    }
                    sib.color = x.p.color;
                    x.p.color = BLACK;
                    sib.right.color = BLACK;
                    rotateLeft(x.p);
                    x = root;
                }
            } else {
                // mirror image case
                Entry sib = x.p.left;
                if (sib.color == RED) {
                    sib.color = BLACK;
                    x.p.color = RED;
                    rotateRight(x.p);
                    sib = x.p.left;
                }
                if (sib.right.color == BLACK && sib.left.color == BLACK) {
                    sib.color = RED;
                    x = x.p;
                } else {
                    if (sib.left.color == BLACK) {
                        sib.right.color = BLACK;
                        sib.color = RED;
                        rotateLeft(sib);
                        sib = x.p.left;
                    }
                    sib.color = x.p.color;
                    x.p.color = BLACK;
                    sib.left.color = BLACK;
                    rotateRight(x.p);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    /** Removes the.
     * 
     * @param z
     *            the z */
    protected void remove(Entry z) {
        boolean isUnique = !(z.keyEquals(z.left) || z.keyEquals(z.right) || z
                .keyEquals(z.p));
        Entry y = z.left != NIL && z.right != NIL ? successor(z) : z;
        Entry x = y.left != NIL ? y.left : y.right;
        x.p = y.p;
        if (y.p == NIL) {
            root = x;
        } else if (y == y.p.left) {
            y.p.left = x;
        } else {
            y.p.right = x;
        }
        if (y != z) {
            z.copyFields(y);
        }
        if (y.color == BLACK) {
            fixUpRemove(x);
        }
        decrementSize(isUnique);
    }

    // ========================================================================
    // Inner classes
    // ------------------------------------------------------------------------
    // Entry class - represents a Red-Black Tree Node
    /** The Class Entry. */
    public static class Entry {
        /** The val. */
        int val;
        /** The order. */
        int order; // used to determine ordering for duplicate keys
        /** The left. */
        Entry left = null;
        /** The right. */
        Entry right = null;
        /** The p. */
        Entry p;
        /** The color. */
        boolean color = BLACK;

        /** Instantiates a new entry.
         * 
         * @param val
         *            the val */
        public Entry(int val) {
            this.val = val;
        }

        /** Instantiates a new entry.
         * 
         * @param val
         *            the val
         * @param parent
         *            the parent
         * @param order
         *            the order */
        public Entry(int val, Entry parent, int order) {
            this.val = val;
            p = parent;
            this.order = order;
            left = NIL;
            right = NIL;
        }

        /** Gets the int key.
         * 
         * @return the int key */
        public int getIntKey() {
            throw new UnsupportedOperationException("Unsupported");
        }

        /** Gets the long key.
         * 
         * @return the long key */
        public long getLongKey() {
            throw new UnsupportedOperationException("Unsupported");
        }

        /** Gets the float key.
         * 
         * @return the float key */
        public float getFloatKey() {
            throw new UnsupportedOperationException("Unsupported");
        }

        /** Gets the double key.
         * 
         * @return the double key */
        public double getDoubleKey() {
            throw new UnsupportedOperationException("Unsupported");
        }

        /** Gets the key.
         * 
         * @return the key */
        public Object getKey() {
            return null;
        }

        /** Gets the value.
         * 
         * @return the value */
        public int getValue() {
            return val;
        }

        /** Gets the order.
         * 
         * @return the order */
        public int getOrder() {
            return order;
        }

        /** Sets the value.
         * 
         * @param value
         *            the value
         * @return the int */
        public int setValue(int value) {
            int old = val;
            val = value;
            return old;
        }

        /** Key equals.
         * 
         * @param e
         *            the e
         * @return true, if successful */
        public boolean keyEquals(Entry e) {
            Object k = getKey();
            return k == null ? k == e.getKey() : k.equals(e.getKey());
        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            return val == e.val && getKey() == e.getKey();
        }

        public int hashCode() {
            int khash = getKey().hashCode();
            int vhash = val;
            return khash ^ vhash;
        }

        public String toString() {
            return getKey() + "=" + val;
        }

        /** Copy fields.
         * 
         * @param x
         *            the x */
        public void copyFields(Entry x) {
            val = x.val;
            order = x.order;
        }
    }

    // ------------------------------------------------------------------------
    // Iterators
    /** The Class EntryIterator. */
    protected class EntryIterator extends AbstractLiteralIterator {
        /** The expected mod count. */
        private int expectedModCount = modCount;
        /** The last returned. */
        private Entry lastReturned = NIL;
        /** The reverse. */
        private boolean reverse = false;
        /** The end. */
        Entry next, end;

        /** Instantiates a new entry iterator.
         * 
         * @param reverse
         *            the reverse */
        EntryIterator(boolean reverse) {
            this.reverse = reverse;
            next = reverse ? maximum(root) : minimum(root);
            end = NIL;
        }

        /** Instantiates a new entry iterator.
         * 
         * @param first
         *            the first
         * @param last
         *            the last */
        EntryIterator(Entry first, Entry last) {
            next = first;
            end = last;
            reverse = first == NIL ? true : last == NIL ? false
                    : compare(first, last) > 0;
        }

        public boolean hasNext() {
            return next != end;
        }

        /** Next entry.
         * 
         * @return the entry */
        final Entry nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            lastReturned = next;
            next = reverse ? predecessor(next) : successor(next);
            // / XXX DEBUG
            if (lastReturned == NIL) {
                System.err.println("Encountered NIL in iteration!");
            }
            return lastReturned;
        }

        public Object next() {
            return nextEntry();
        }

        public void remove() {
            if (lastReturned == NIL) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturned.left != NIL && lastReturned.right != NIL) {
                next = lastReturned;
            }
            AbstractTreeMap.this.remove(lastReturned);
            ++expectedModCount;
            lastReturned = NIL;
        }
    }

    /** The Class KeyIterator. */
    protected class KeyIterator extends EntryIterator {
        /** Instantiates a new key iterator. */
        public KeyIterator() {
            super(false);
        }

        /** Instantiates a new key iterator.
         * 
         * @param start
         *            the start
         * @param end
         *            the end */
        public KeyIterator(Entry start, Entry end) {
            super(start, end);
        }

        public Object next() {
            return nextEntry().getKey();
        }
    }

    /** The Class ValueIterator. */
    protected class ValueIterator extends IntIterator {
        /** The m_iter. */
        EntryIterator m_iter;

        /** Instantiates a new value iterator.
         * 
         * @param iter
         *            the iter */
        public ValueIterator(EntryIterator iter) {
            m_iter = iter;
        }

        public boolean hasNext() {
            return m_iter.hasNext();
        }

        public int nextInt() {
            return m_iter.nextEntry().val;
        }

        public void remove() {
            m_iter.remove();
        }
    }
} // end of abstract class AbstractTreeMap
