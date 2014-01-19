package prefuse.util.collections;

import java.util.Comparator;

/** The Interface LiteralComparator.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public interface LiteralComparator extends Comparator {
    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int */
    int compare(byte x1, byte x2);

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int */
    int compare(int x1, int x2);

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int */
    int compare(long x1, long x2);

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int */
    int compare(float x1, float x2);

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int */
    int compare(double x1, double x2);

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int */
    int compare(boolean x1, boolean x2);
} // end of interface LiteralComparator
