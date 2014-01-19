package prefuse.util.collections;

/** Sorted map implementation using a red-black tree to map from double keys to
 * int values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class DoubleIntTreeMap extends AbstractTreeMap implements DoubleIntSortedMap {
    // dummy entry used as wrapper for queries
    /** The dummy. */
    private final DoubleEntry dummy = new DoubleEntry(Double.MIN_VALUE,
            Integer.MAX_VALUE, NIL, 0);

    // ------------------------------------------------------------------------
    // Constructors
    /** Instantiates a new double int tree map. */
    public DoubleIntTreeMap() {
        this(null, false);
    }

    /** Instantiates a new double int tree map.
     * 
     * @param allowDuplicates
     *            the allow duplicates */
    public DoubleIntTreeMap(boolean allowDuplicates) {
        this(null, allowDuplicates);
    }

    /** Instantiates a new double int tree map.
     * 
     * @param comparator
     *            the comparator */
    public DoubleIntTreeMap(LiteralComparator comparator) {
        this(comparator, false);
    }

    /** Instantiates a new double int tree map.
     * 
     * @param comparator
     *            the comparator
     * @param allowDuplicates
     *            the allow duplicates */
    public DoubleIntTreeMap(LiteralComparator comparator, boolean allowDuplicates) {
        super(comparator, allowDuplicates);
    }

    // ------------------------------------------------------------------------
    // SortedMap Methods
    /** Clear.
     * 
     * @see java.util.Map#clear() */
    @Override
    public void clear() {
        ++modCount;
        size = 0;
        root = NIL;
    }

    /** Contains key.
     * 
     * @param key
     *            the key
     * @return true, if successful
     * @see java.util.Map#containsKey(java.lang.Object) */
    @Override
    public boolean containsKey(double key) {
        return find(key, 0) != NIL;
    }

    /** Gets the.
     * 
     * @param key
     *            the key
     * @return the int
     * @see java.util.Map#get(java.lang.Object) */
    @Override
    public int get(double key) {
        Entry ret = find(key, 0);
        return ret == NIL ? Integer.MIN_VALUE : ret.val;
    }

    /** Put.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the int
     * @see java.util.Map#put(java.lang.Object, java.lang.Object) */
    @Override
    public int put(double key, int value) {
        Entry t = root;
        lastOrder = 0;
        if (t == NIL) {
            incrementSize(true);
            root = new DoubleEntry(key, value, NIL, lastOrder);
            return Integer.MIN_VALUE;
        }
        dummy.key = key;
        dummy.order = Integer.MAX_VALUE;
        while (true) {
            int cmp = compare(dummy, t);
            if (cmp == 0) {
                return t.setValue(value);
            } else if (cmp < 0) {
                if (t.left != NIL) {
                    t = t.left;
                } else {
                    incrementSize(lastOrder == 0);
                    t.left = new DoubleEntry(key, value, t, lastOrder);
                    fixUpInsert(t.left);
                    return Integer.MIN_VALUE;
                }
            } else { // cmp > 0
                if (t.right != NIL) {
                    t = t.right;
                } else {
                    incrementSize(lastOrder == 0);
                    t.right = new DoubleEntry(key, value, t, lastOrder);
                    fixUpInsert(t.right);
                    return Integer.MIN_VALUE;
                }
            }
        }
    }

    /** Removes the.
     * 
     * @param key
     *            the key
     * @return the int
     * @see java.util.Map#remove(java.lang.Object) */
    @Override
    public int remove(double key) {
        // remove the last instance with the given key
        Entry x;
        if (allowDuplicates) {
            x = findPredecessor(key, Integer.MAX_VALUE);
        } else {
            x = find(key, 0);
        }
        if (x == NIL) {
            return Integer.MIN_VALUE;
        }
        int val = x.val;
        remove(x);
        return val;
    }

    @Override
    public int remove(double key, int val) {
        // remove the last instance with the given key
        Entry x = findCeiling(key, 0);
        if (x != NIL && x.getDoubleKey() != key) {
            x = successor(x);
        }
        if (x == NIL || x.getDoubleKey() != key) {
            return Integer.MIN_VALUE;
        }
        for (; x.val != val && x != NIL; x = successor(x)) {
            ;
        }
        if (x == NIL) {
            return Integer.MIN_VALUE;
        }
        remove(x);
        return val;
    }

    /** First key.
     * 
     * @return the double
     * @see java.util.SortedMap#firstKey() */
    @Override
    public double firstKey() {
        return minimum(root).getDoubleKey();
    }

    /** Last key.
     * 
     * @return the double
     * @see java.util.SortedMap#lastKey() */
    @Override
    public double lastKey() {
        return maximum(root).getDoubleKey();
    }

    // -- Collection view methods ---------------------------------------------
    @Override
    public LiteralIterator keyIterator() {
        return new KeyIterator();
    }

    @Override
    public LiteralIterator keyRangeIterator(double fromKey, boolean fromInc,
            double toKey, boolean toInc) {
        Entry start, end;
        if (cmp.compare(fromKey, toKey) <= 0) {
            start = findCeiling(fromKey, fromInc ? 0 : Integer.MAX_VALUE);
            end = findCeiling(toKey, toInc ? Integer.MAX_VALUE : 0);
        } else {
            start = findCeiling(fromKey, fromInc ? Integer.MAX_VALUE : 0);
            start = predecessor(start);
            end = findCeiling(toKey, toInc ? 0 : Integer.MAX_VALUE);
            end = predecessor(end);
        }
        return new KeyIterator(start, end);
    }

    @Override
    public IntIterator valueRangeIterator(double fromKey, boolean fromInc, double toKey,
            boolean toInc) {
        return new ValueIterator((EntryIterator) keyRangeIterator(fromKey, fromInc,
                toKey, toInc));
    }

    // ------------------------------------------------------------------------
    // Internal Binary Search Tree / Red-Black Tree methods
    // Adapted from Cormen, Leiserson, and Rivest's Introduction to Algorithms
    @Override
    protected int compare(Entry e1, Entry e2) {
        int c = cmp.compare(e1.getDoubleKey(), e2.getDoubleKey());
        if (allowDuplicates) {
            if (c == 0) {
                c = e1.order < e2.order ? -1 : e1.order > e2.order ? 1 : 0;
                lastOrder = 1 + (c < 0 ? e1.order : e2.order);
            }
        }
        return c;
    }

    /** Find.
     * 
     * @param key
     *            the key
     * @param order
     *            the order
     * @return the entry */
    private Entry find(double key, int order) {
        dummy.key = key;
        dummy.order = order;
        Entry e = find(dummy);
        return e;
    }

    /** Find predecessor.
     * 
     * @param key
     *            the key
     * @param order
     *            the order
     * @return the entry */
    private Entry findPredecessor(double key, int order) {
        dummy.key = key;
        dummy.order = order;
        Entry e = findPredecessor(dummy);
        return e;
    }

    /** Find ceiling.
     * 
     * @param key
     *            the key
     * @param order
     *            the order
     * @return the entry */
    private Entry findCeiling(double key, int order) {
        dummy.key = key;
        dummy.order = order;
        Entry e = findCeiling(dummy);
        return e;
    }

    // ========================================================================
    // Inner classes
    // ------------------------------------------------------------------------
    // Entry class - represents a Red-Black Tree Node
    /** The Class DoubleEntry. */
    static class DoubleEntry extends AbstractTreeMap.Entry {
        /** The key. */
        double key;

        /** Instantiates a new double entry.
         * 
         * @param key
         *            the key
         * @param val
         *            the val */
        public DoubleEntry(double key, int val) {
            super(val);
            this.key = key;
        }

        /** Instantiates a new double entry.
         * 
         * @param key
         *            the key
         * @param val
         *            the val
         * @param parent
         *            the parent
         * @param order
         *            the order */
        public DoubleEntry(double key, int val, Entry parent, int order) {
            super(val, parent, order);
            this.key = key;
        }

        @Override
        public double getDoubleKey() {
            return key;
        }

        @Override
        public Object getKey() {
            return new Double(key);
        }

        @Override
        public boolean keyEquals(Entry e) {
            return e instanceof DoubleEntry && key == ((DoubleEntry) e).key;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof DoubleEntry)) {
                return false;
            }
            DoubleEntry e = (DoubleEntry) o;
            return key == e.key && val == e.val;
        }

        @Override
        public int hashCode() {
            long k = Double.doubleToLongBits(key);
            int khash = (int) (k ^ k >>> 32);
            int vhash = val;
            return khash ^ vhash ^ order;
        }

        @Override
        public String toString() {
            return key + "=" + val;
        }

        @Override
        public void copyFields(Entry x) {
            super.copyFields(x);
            key = x.getDoubleKey();
        }
    }

    // ------------------------------------------------------------------------
    // Iterators
    /** The Class KeyIterator. */
    private class KeyIterator extends AbstractTreeMap.KeyIterator {
        /** Instantiates a new key iterator. */
        public KeyIterator() {
            super();
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

        @Override
        public boolean isDoubleSupported() {
            return true;
        }

        @Override
        public double nextDouble() {
            return nextEntry().getDoubleKey();
        }
    }
} // end of class DoubleIntTreeMap
