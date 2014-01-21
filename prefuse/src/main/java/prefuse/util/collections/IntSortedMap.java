package prefuse.util.collections;

import java.util.Comparator;

/** The Interface IntSortedMap.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public interface IntSortedMap {
    /** Gets the minimum.
     * 
     * @return the minimum */
    public int getMinimum();

    /** Gets the maximum.
     * 
     * @return the maximum */
    public int getMaximum();

    /** Gets the median.
     * 
     * @return the median */
    public int getMedian();

    /** Gets the unique count.
     * 
     * @return the unique count */
    public int getUniqueCount();

    /** Checks if is allow duplicates.
     * 
     * @return true, if is allow duplicates */
    public boolean isAllowDuplicates();

    /** Size.
     * 
     * @return the int */
    public int size();

    /** Checks if is empty.
     * 
     * @return true, if is empty */
    public boolean isEmpty();

    /** Comparator.
     * 
     * @return the comparator */
    public Comparator comparator();

    /** Clear. */
    public void clear();

    /** Contains value.
     * 
     * @param value
     *            the value
     * @return true, if successful */
    public boolean containsValue(int value);

    /** Value iterator.
     * 
     * @param ascending
     *            the ascending
     * @return the int iterator */
    public IntIterator valueIterator(boolean ascending);
}
