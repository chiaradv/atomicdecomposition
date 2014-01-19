package prefuse.util.collections;

/** Sorted map that maps from an int key to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public interface IntIntSortedMap extends IntSortedMap {
    /** First key.
     * 
     * @return the int */
    public int firstKey();

    /** Last key.
     * 
     * @return the int */
    public int lastKey();

    /** Contains key.
     * 
     * @param key
     *            the key
     * @return true, if successful */
    public boolean containsKey(int key);

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
    public IntIterator valueRangeIterator(int fromKey, boolean fromInc, int toKey,
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
    public LiteralIterator keyRangeIterator(int fromKey, boolean fromInc, int toKey,
            boolean toInc);

    /** Gets the.
     * 
     * @param key
     *            the key
     * @return the int */
    public int get(int key);

    /** Removes the.
     * 
     * @param key
     *            the key
     * @return the int */
    public int remove(int key);

    /** Removes the.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the int */
    public int remove(int key, int value);

    /** Put.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the int */
    public int put(int key, int value);

    /** Gets the last.
     * 
     * @param key
     *            the key
     * @return the last */
    public int getLast(int key);

    /** Gets the next value.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the next value */
    public int getNextValue(int key, int value);

    /** Gets the previous value.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the previous value */
    public int getPreviousValue(int key, int value);
} // end of interface IntIntSortedMap
