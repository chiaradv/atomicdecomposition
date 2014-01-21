package prefuse.util.collections;

import java.util.Iterator;

/** The Interface LiteralIterator.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public interface LiteralIterator extends Iterator {
    /** Next int.
     * 
     * @return the int */
    public int nextInt();

    /** Checks if is int supported.
     * 
     * @return true, if is int supported */
    public boolean isIntSupported();

    /** Next long.
     * 
     * @return the long */
    public long nextLong();

    /** Checks if is long supported.
     * 
     * @return true, if is long supported */
    public boolean isLongSupported();

    /** Next float.
     * 
     * @return the float */
    public float nextFloat();

    /** Checks if is float supported.
     * 
     * @return true, if is float supported */
    public boolean isFloatSupported();

    /** Next double.
     * 
     * @return the double */
    public double nextDouble();

    /** Checks if is double supported.
     * 
     * @return true, if is double supported */
    public boolean isDoubleSupported();

    /** Next boolean.
     * 
     * @return true, if successful */
    public boolean nextBoolean();

    /** Checks if is boolean supported.
     * 
     * @return true, if is boolean supported */
    public boolean isBooleanSupported();
} // end of interface LiteralIterator
