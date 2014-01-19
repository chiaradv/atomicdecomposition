package prefuse.util.collections;


/**
 * Sorted map that maps from a long key to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface LongIntSortedMap extends IntSortedMap {

    /**
     * First key.
     *
     * @return the long
     */
    public long firstKey();

    /**
     * Last key.
     *
     * @return the long
     */
    public long lastKey();

    /**
     * Contains key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsKey(long key);
    
    /**
     * Value range iterator.
     *
     * @param fromKey the from key
     * @param fromInc the from inc
     * @param toKey the to key
     * @param toInc the to inc
     * @return the int iterator
     */
    public IntIterator valueRangeIterator(long fromKey, boolean fromInc, 
                                          long toKey,   boolean toInc);
    
    /**
     * Key iterator.
     *
     * @return the literal iterator
     */
    public LiteralIterator keyIterator();

    /**
     * Key range iterator.
     *
     * @param fromKey the from key
     * @param fromInc the from inc
     * @param toKey the to key
     * @param toInc the to inc
     * @return the literal iterator
     */
    public LiteralIterator keyRangeIterator(long fromKey, boolean fromInc, 
                                            long toKey,   boolean toInc);

    /**
     * Gets the.
     *
     * @param key the key
     * @return the int
     */
    public int get(long key);

    /**
     * Removes the.
     *
     * @param key the key
     * @return the int
     */
    public int remove(long key);
    
    /**
     * Removes the.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int remove(long key, int value);

    /**
     * Put.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int put(long key, int value);
    
} // end of interface LongIntSortedMap
