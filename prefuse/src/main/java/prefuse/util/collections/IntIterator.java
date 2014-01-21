package prefuse.util.collections;

/** Abstract LiteralIterator implementation that supports an iteration over int
 * values. Subclasses need only implement the {@link #nextInt()} method. The
 * {@link #nextLong()}, {@link #nextFloat()}, and {@link #nextDouble()} methods
 * all simply cast the output of {@link #nextInt()}. The {@link #next()} method
 * simply wraps the output of {@link #nextInt()} in an {@link java.lang.Integer}
 * object.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class IntIterator extends AbstractLiteralIterator {
    /** Next.
     * 
     * @return the object
     * @see java.util.Iterator#next() */
    @Override
    public Object next() {
        return new Integer(nextInt());
    }

    /** Checks if is double supported.
     * 
     * @return true, if is double supported
     * @see prefuse.util.collections.LiteralIterator#isDoubleSupported() */
    @Override
    public boolean isDoubleSupported() {
        return true;
    }

    /** Checks if is float supported.
     * 
     * @return true, if is float supported
     * @see prefuse.util.collections.LiteralIterator#isFloatSupported() */
    @Override
    public boolean isFloatSupported() {
        return true;
    }

    /** Checks if is int supported.
     * 
     * @return true, if is int supported
     * @see prefuse.util.collections.LiteralIterator#isIntSupported() */
    @Override
    public boolean isIntSupported() {
        return true;
    }

    /** Checks if is long supported.
     * 
     * @return true, if is long supported
     * @see prefuse.util.collections.LiteralIterator#isLongSupported() */
    @Override
    public boolean isLongSupported() {
        return true;
    }

    /** Next double.
     * 
     * @return the double
     * @see prefuse.util.collections.LiteralIterator#nextDouble() */
    @Override
    public double nextDouble() {
        return nextInt();
    }

    /** Next float.
     * 
     * @return the float
     * @see prefuse.util.collections.LiteralIterator#nextFloat() */
    @Override
    public float nextFloat() {
        return nextInt();
    }

    /** Next long.
     * 
     * @return the long
     * @see prefuse.util.collections.LiteralIterator#nextLong() */
    @Override
    public long nextLong() {
        return nextInt();
    }

    /** Next int.
     * 
     * @return the int
     * @see prefuse.util.collections.LiteralIterator#nextInt() */
    @Override
    public abstract int nextInt();
} // end of abstract class IntIterator
