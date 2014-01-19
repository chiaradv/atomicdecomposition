package prefuse.util.collections;


/**
 * Sorted map that maps from a boolean key to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface BooleanIntSortedMap extends IntSortedMap {

    /**
     * First key.
     *
     * @return true, if successful
     */
    public boolean firstKey();

    /**
     * Last key.
     *
     * @return true, if successful
     */
    public boolean lastKey();

    /**
     * Contains key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsKey(boolean key);
    
    /**
     * Value range iterator.
     *
     * @param fromKey the from key
     * @param fromInc the from inc
     * @param toKey the to key
     * @param toInc the to inc
     * @return the int iterator
     */
    public IntIterator valueRangeIterator(boolean fromKey, boolean fromInc, 
                                          boolean toKey,   boolean toInc);
    
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
    public LiteralIterator keyRangeIterator(boolean fromKey, boolean fromInc, 
                                            boolean toKey,   boolean toInc);

    /**
     * Gets the.
     *
     * @param key the key
     * @return the int
     */
    public int get(boolean key);

    /**
     * Removes the.
     *
     * @param key the key
     * @return the int
     */
    public int remove(boolean key);
    
    /**
     * Removes the.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int remove(boolean key, int value);

    /**
     * Put.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int put(boolean key, int value);
    
} // end of interface LongIntSortedMap
