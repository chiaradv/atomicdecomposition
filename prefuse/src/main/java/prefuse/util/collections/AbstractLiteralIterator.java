package prefuse.util.collections;

/** Abstract base class for a LiteralIterator implementations.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractLiteralIterator implements LiteralIterator {
    /** Next int.
     * 
     * @return the int
     * @see prefuse.util.collections.LiteralIterator#nextInt() */
    @Override
    public int nextInt() {
        throw new UnsupportedOperationException("int type unsupported");
    }

    /** Next long.
     * 
     * @return the long
     * @see prefuse.util.collections.LiteralIterator#nextLong() */
    @Override
    public long nextLong() {
        throw new UnsupportedOperationException("long type unsupported");
    }

    /** Next float.
     * 
     * @return the float
     * @see prefuse.util.collections.LiteralIterator#nextFloat() */
    @Override
    public float nextFloat() {
        throw new UnsupportedOperationException("float type unsupported");
    }

    /** Next double.
     * 
     * @return the double
     * @see prefuse.util.collections.LiteralIterator#nextDouble() */
    @Override
    public double nextDouble() {
        throw new UnsupportedOperationException("double type unsupported");
    }

    /** Next boolean.
     * 
     * @return true, if successful
     * @see prefuse.util.collections.LiteralIterator#nextBoolean() */
    @Override
    public boolean nextBoolean() {
        throw new UnsupportedOperationException("boolean type unsupported");
    }

    /** Checks if is boolean supported.
     * 
     * @return true, if is boolean supported
     * @see prefuse.util.collections.LiteralIterator#isBooleanSupported() */
    @Override
    public boolean isBooleanSupported() {
        return false;
    }

    /** Checks if is double supported.
     * 
     * @return true, if is double supported
     * @see prefuse.util.collections.LiteralIterator#isDoubleSupported() */
    @Override
    public boolean isDoubleSupported() {
        return false;
    }

    /** Checks if is float supported.
     * 
     * @return true, if is float supported
     * @see prefuse.util.collections.LiteralIterator#isFloatSupported() */
    @Override
    public boolean isFloatSupported() {
        return false;
    }

    /** Checks if is int supported.
     * 
     * @return true, if is int supported
     * @see prefuse.util.collections.LiteralIterator#isIntSupported() */
    @Override
    public boolean isIntSupported() {
        return false;
    }

    /** Checks if is long supported.
     * 
     * @return true, if is long supported
     * @see prefuse.util.collections.LiteralIterator#isLongSupported() */
    @Override
    public boolean isLongSupported() {
        return false;
    }
} // end of class AbstractLiteralIterator
