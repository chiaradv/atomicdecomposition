package prefuse.util.collections;

import java.util.Iterator;


/**
 * Sorted map that maps from an Object key to an int value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface ObjectIntSortedMap extends IntSortedMap {

    /** The Constant MAX_KEY. */
    public static final Object MAX_KEY = new Object();
    
    /** The Constant MIN_KEY. */
    public static final Object MIN_KEY = new Object();
    
    /**
     * First key.
     *
     * @return the object
     */
    public Object firstKey();

    /**
     * Last key.
     *
     * @return the object
     */
    public Object lastKey();

    /**
     * Contains key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsKey(Object key);
    
    /**
     * Value range iterator.
     *
     * @param fromKey the from key
     * @param fromInc the from inc
     * @param toKey the to key
     * @param toInc the to inc
     * @return the int iterator
     */
    public IntIterator valueRangeIterator(Object fromKey, boolean fromInc, 
                                          Object toKey,   boolean toInc);
    
    /**
     * Key iterator.
     *
     * @return the iterator
     */
    public Iterator keyIterator();

    /**
     * Key range iterator.
     *
     * @param fromKey the from key
     * @param fromInc the from inc
     * @param toKey the to key
     * @param toInc the to inc
     * @return the iterator
     */
    public Iterator keyRangeIterator(Object fromKey, boolean fromInc, 
                                     Object toKey,   boolean toInc);

    /**
     * Gets the.
     *
     * @param key the key
     * @return the int
     */
    public int get(Object key);

    /**
     * Removes the.
     *
     * @param key the key
     * @return the int
     */
    public int remove(Object key);
    
    /**
     * Removes the.
     *
     * @param key the key
     * @param val the val
     * @return the int
     */
    public int remove(Object key, int val);

    /**
     * Put.
     *
     * @param key the key
     * @param value the value
     * @return the int
     */
    public int put(Object key, int value);
    
} // end of interface ObjectIntSortedMap
