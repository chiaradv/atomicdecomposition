package prefuse.util.collections;

/** Sorted map implementation using a red-black tree to map from long keys to int
 * values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class LongIntTreeMap extends AbstractTreeMap implements LongIntSortedMap {
    // dummy entry used as wrapper for queries
    /** The dummy. */
    private LongEntry dummy = new LongEntry(Long.MIN_VALUE, Integer.MAX_VALUE, NIL, 0);

    // ------------------------------------------------------------------------
    // Constructors
    /** Instantiates a new long int tree map. */
    public LongIntTreeMap() {
        this(null, false);
    }

    /** Instantiates a new long int tree map.
     * 
     * @param allowDuplicates
     *            the allow duplicates */
    public LongIntTreeMap(boolean allowDuplicates) {
        this(null, allowDuplicates);
    }

    /** Instantiates a new long int tree map.
     * 
     * @param comparator
     *            the comparator */
    public LongIntTreeMap(LiteralComparator comparator) {
        this(comparator, false);
    }

    /** Instantiates a new long int tree map.
     * 
     * @param comparator
     *            the comparator
     * @param allowDuplicates
     *            the allow duplicates */
    public LongIntTreeMap(LiteralComparator comparator, boolean allowDuplicates) {
        super(comparator, allowDuplicates);
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

    /** Contains key.
     * 
     * @param key
     *            the key
     * @return true, if successful
     * @see java.util.Map#containsKey(java.lang.Object) */
    public boolean containsKey(long key) {
        return find(key, 0) != NIL;
    }

    /** Gets the.
     * 
     * @param key
     *            the key
     * @return the int
     * @see java.util.Map#get(java.lang.Object) */
    public int get(long key) {
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
    public int put(long key, int value) {
        Entry t = root;
        lastOrder = 0;
        if (t == NIL) {
            incrementSize(true);
            root = new LongEntry(key, value, NIL, lastOrder);
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
                    t.left = new LongEntry(key, value, t, lastOrder);
                    fixUpInsert(t.left);
                    return Integer.MIN_VALUE;
                }
            } else { // cmp > 0
                if (t.right != NIL) {
                    t = t.right;
                } else {
                    incrementSize(lastOrder == 0);
                    t.right = new LongEntry(key, value, t, lastOrder);
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
    public int remove(long key) {
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

    public int remove(long key, int val) {
        // remove the last instance with the given key
        Entry x = findCeiling(key, 0);
        if (x != NIL && x.getLongKey() != key) {
            x = successor(x);
        }
        if (x == NIL || x.getLongKey() != key) {
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
     * @return the long
     * @see java.util.SortedMap#firstKey() */
    public long firstKey() {
        return minimum(root).getLongKey();
    }

    /** Last key.
     * 
     * @return the long
     * @see java.util.SortedMap#lastKey() */
    public long lastKey() {
        return maximum(root).getLongKey();
    }

    // -- Collection view methods ---------------------------------------------
    public LiteralIterator keyIterator() {
        return new KeyIterator();
    }

    public LiteralIterator keyRangeIterator(long fromKey, boolean fromInc, long toKey,
            boolean toInc) {
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

    public IntIterator valueRangeIterator(long fromKey, boolean fromInc, long toKey,
            boolean toInc) {
        return new ValueIterator((EntryIterator) keyRangeIterator(fromKey, fromInc,
                toKey, toInc));
    }

    // ------------------------------------------------------------------------
    // Internal Binary Search Tree / Red-Black Tree methods
    // Adapted from Cormen, Leiserson, and Rivest's Introduction to Algorithms
    protected int compare(Entry e1, Entry e2) {
        int c = cmp.compare(e1.getLongKey(), e2.getLongKey());
        if (allowDuplicates && c == 0) {
            c = e1.order < e2.order ? -1 : e1.order > e2.order ? 1 : 0;
            lastOrder = 1 + (c < 0 ? e1.order : e2.order);
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
    private Entry find(long key, int order) {
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
    private Entry findPredecessor(long key, int order) {
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
    private Entry findCeiling(long key, int order) {
        dummy.key = key;
        dummy.order = order;
        Entry e = findCeiling(dummy);
        return e;
    }

    // ========================================================================
    // Inner classes
    // ------------------------------------------------------------------------
    // Entry class - represents a Red-Black Tree Node
    /** The Class LongEntry. */
    static class LongEntry extends AbstractTreeMap.Entry {
        /** The key. */
        long key;

        /** Instantiates a new long entry.
         * 
         * @param key
         *            the key
         * @param val
         *            the val */
        public LongEntry(long key, int val) {
            super(val);
            this.key = key;
        }

        /** Instantiates a new long entry.
         * 
         * @param key
         *            the key
         * @param val
         *            the val
         * @param parent
         *            the parent
         * @param order
         *            the order */
        public LongEntry(long key, int val, Entry parent, int order) {
            super(val, parent, order);
            this.key = key;
        }

        public long getLongKey() {
            return key;
        }

        public Object getKey() {
            return new Long(key);
        }

        public boolean keyEquals(Entry e) {
            return e instanceof LongEntry && key == ((LongEntry) e).key;
        }

        public boolean equals(Object o) {
            if (!(o instanceof LongEntry)) {
                return false;
            }
            LongEntry e = (LongEntry) o;
            return key == e.key && val == e.val;
        }

        public int hashCode() {
            int khash = (int) (key ^ key >>> 32);
            int vhash = val;
            return khash ^ vhash ^ order;
        }

        public String toString() {
            return key + "=" + val;
        }

        public void copyFields(Entry x) {
            super.copyFields(x);
            key = x.getLongKey();
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

        public boolean isLongSupported() {
            return true;
        }

        public long nextLong() {
            return nextEntry().getLongKey();
        }
    }
} // end of class LongIntTreeMap
