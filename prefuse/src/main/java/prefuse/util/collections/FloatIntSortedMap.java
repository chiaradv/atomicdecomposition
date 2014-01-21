package prefuse.util.collections;

/** Sorted map that maps from a float key to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public interface FloatIntSortedMap extends IntSortedMap {
    /** First key.
     * 
     * @return the float */
    public float firstKey();

    /** Last key.
     * 
     * @return the float */
    public float lastKey();

    /** Contains key.
     * 
     * @param key
     *            the key
     * @return true, if successful */
    public boolean containsKey(float key);

    /** Value range iterator.
     * 
     * @param fromKey
     *            the from key
     * @param fromInc
     *            the from inc
     * @param toKey
     *            the to key
     * @param toInc
     *            the to inc
     * @return the int iterator */
    public IntIterator valueRangeIterator(float fromKey, boolean fromInc, float toKey,
            boolean toInc);

    /** Key iterator.
     * 
     * @return the literal iterator */
    public LiteralIterator keyIterator();

    /** Key range iterator.
     * 
     * @param fromKey
     *            the from key
     * @param fromInc
     *            the from inc
     * @param toKey
     *            the to key
     * @param toInc
     *            the to inc
     * @return the literal iterator */
    public LiteralIterator keyRangeIterator(float fromKey, boolean fromInc, float toKey,
            boolean toInc);

    /** Gets the.
     * 
     * @param key
     *            the key
     * @return the int */
    public int get(float key);

    /** Removes the.
     * 
     * @param key
     *            the key
     * @return the int */
    public int remove(float key);

    /** Removes the.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the int */
    public int remove(float key, int value);

    /** Put.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the int */
    public int put(float key, int value);
} // end of interface FloatIntSortedMap
