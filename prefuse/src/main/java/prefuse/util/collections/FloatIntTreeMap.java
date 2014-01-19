package prefuse.util.collections;

/** Sorted map implementation using a red-black tree to map from float keys to
 * int values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class FloatIntTreeMap extends AbstractTreeMap implements FloatIntSortedMap {
    // dummy entry used as wrapper for queries
    /** The dummy. */
    private FloatEntry dummy = new FloatEntry(Float.MIN_VALUE, Integer.MAX_VALUE, NIL, 0);

    // ------------------------------------------------------------------------
    // Constructors
    /** Instantiates a new float int tree map. */
    public FloatIntTreeMap() {
        this(null, false);
    }

    /** Instantiates a new float int tree map.
     * 
     * @param allowDuplicates
     *            the allow duplicates */
    public FloatIntTreeMap(boolean allowDuplicates) {
        this(null, allowDuplicates);
    }

    /** Instantiates a new float int tree map.
     * 
     * @param comparator
     *            the comparator */
    public FloatIntTreeMap(LiteralComparator comparator) {
        this(comparator, false);
    }

    /** Instantiates a new float int tree map.
     * 
     * @param comparator
     *            the comparator
     * @param allowDuplicates
     *            the allow duplicates */
    public FloatIntTreeMap(LiteralComparator comparator, boolean allowDuplicates) {
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
    public boolean containsKey(float key) {
        return find(key, 0) != NIL;
    }

    /** Gets the.
     * 
     * @param key
     *            the key
     * @return the int
     * @see java.util.Map#get(java.lang.Object) */
    public int get(float key) {
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
    public int put(float key, int value) {
        Entry t = root;
        lastOrder = 0;
        if (t == NIL) {
            incrementSize(true);
            root = new FloatEntry(key, value, NIL, lastOrder);
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
                    t.left = new FloatEntry(key, value, t, lastOrder);
                    fixUpInsert(t.left);
                    return Integer.MIN_VALUE;
                }
            } else { // cmp > 0
                if (t.right != NIL) {
                    t = t.right;
                } else {
                    incrementSize(lastOrder == 0);
                    t.right = new FloatEntry(key, value, t, lastOrder);
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
    public int remove(float key) {
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

    public int remove(float key, int val) {
        // remove the last instance with the given key
        Entry x = findCeiling(key, 0);
        if (x != NIL && x.getFloatKey() != key) {
            x = successor(x);
        }
        if (x == NIL || x.getFloatKey() != key) {
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
     * @return the float
     * @see java.util.SortedMap#firstKey() */
    public float firstKey() {
        return minimum(root).getFloatKey();
    }

    /** Last key.
     * 
     * @return the float
     * @see java.util.SortedMap#lastKey() */
    public float lastKey() {
        return maximum(root).getFloatKey();
    }

    // -- Collection view methods ---------------------------------------------
    public LiteralIterator keyIterator() {
        return new KeyIterator();
    }

    public LiteralIterator keyRangeIterator(float fromKey, boolean fromInc, float toKey,
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

    public IntIterator valueRangeIterator(float fromKey, boolean fromInc, float toKey,
            boolean toInc) {
        return new ValueIterator((EntryIterator) keyRangeIterator(fromKey, fromInc,
                toKey, toInc));
    }

    // ------------------------------------------------------------------------
    // Internal Binary Search Tree / Red-Black Tree methods
    // Adapted from Cormen, Leiserson, and Rivest's Introduction to Algorithms
    protected int compare(Entry e1, Entry e2) {
        int c = cmp.compare(e1.getFloatKey(), e2.getFloatKey());
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
    private Entry find(float key, int order) {
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
    private Entry findPredecessor(float key, int order) {
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
    private Entry findCeiling(float key, int order) {
        dummy.key = key;
        dummy.order = order;
        Entry e = findCeiling(dummy);
        return e;
    }

    // ========================================================================
    // Inner classes
    // ------------------------------------------------------------------------
    // Entry class - represents a Red-Black Tree Node
    /** The Class FloatEntry. */
    static class FloatEntry extends AbstractTreeMap.Entry {
        /** The key. */
        float key;

        /** Instantiates a new float entry.
         * 
         * @param key
         *            the key
         * @param val
         *            the val */
        public FloatEntry(float key, int val) {
            super(val);
            this.key = key;
        }

        /** Instantiates a new float entry.
         * 
         * @param key
         *            the key
         * @param val
         *            the val
         * @param parent
         *            the parent
         * @param order
         *            the order */
        public FloatEntry(float key, int val, Entry parent, int order) {
            super(val, parent, order);
            this.key = key;
        }

        public float getFloatKey() {
            return key;
        }

        public Object getKey() {
            return new Float(key);
        }

        public boolean keyEquals(Entry e) {
            return e instanceof FloatEntry && key == ((FloatEntry) e).key;
        }

        public boolean equals(Object o) {
            if (!(o instanceof FloatEntry)) {
                return false;
            }
            FloatEntry e = (FloatEntry) o;
            return key == e.key && val == e.val;
        }

        public int hashCode() {
            int khash = Float.floatToIntBits(key);
            int vhash = val;
            return khash ^ vhash ^ order;
        }

        public String toString() {
            return key + "=" + val;
        }

        public void copyFields(Entry x) {
            super.copyFields(x);
            key = x.getFloatKey();
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

        public boolean isFloatSupported() {
            return true;
        }

        public float nextFloat() {
            return nextEntry().getFloatKey();
        }
    }
} // end of class FloatIntTreeMap
