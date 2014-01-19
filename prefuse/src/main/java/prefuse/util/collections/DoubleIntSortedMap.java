package prefuse.util.collections;



/**
 * Sorted map that maps from a double key to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface DoubleIntSortedMap extends IntSortedMap {

    /**
     * First key.
     *
     * @return the double
     */
    public double firstKey();

    /**
     * Last key.
     *
     * @return the double
     */
    public double lastKey();

    /**
     * Contains key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsKey(double key);
    
    /**
     * Value range iterator.
     *
     * @param fromKey the from key
     * @param fromInc the from inc
     * @param toKey the to key
     * @param toInc the to inc
     * @return the int iterator
     */
    public IntIterator valueRangeIterator(double fromKey, boolean fromInc, 
                                          double toKey,   boolean toInc);
    
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
    public LiteralIterator keyRangeIterator(double fromKey, boolean fromInc, 
                                            double toKey,   boolean toInc);

    /**
     * Gets the.
     *
     * @param key the key
     * @return the int
     */
    public int get(double key);

    /**
     * Removes the.
     *
     * @param key the key
     * @return the int
     */
    public int remove(double key);
    
    /**
     * Removes the.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int remove(double key, int value);

    /**
     * Put.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int put(double key, int value);
    
} // end of interface DoubleIntSortedMap
