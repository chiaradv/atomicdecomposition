package prefuse.util.collections;

/** Default LiteralComparator implementation that uses the natural ordering of
 * all data types for comparing values. Object values will need to implement the
 * {@link java.lang.Comparable} interface.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class DefaultLiteralComparator implements LiteralComparator {
    // maintain a singleton instance of this class
    /** The s_instance. */
    private static DefaultLiteralComparator s_instance = null;

    /** Returns an instance of this comparator.
     * 
     * @return a DefaultLiteralComparator */
    public static DefaultLiteralComparator getInstance() {
        if (s_instance == null) {
            s_instance = new DefaultLiteralComparator();
        }
        return s_instance;
    }

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int
     * @see prefuse.util.collections.LiteralComparator#compare(byte, byte) */
    @Override
    public int compare(byte x1, byte x2) {
        return x1 < x2 ? -1 : x1 > x2 ? 1 : 0;
    }

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int
     * @see prefuse.util.collections.LiteralComparator#compare(int, int) */
    @Override
    public int compare(int x1, int x2) {
        return x1 < x2 ? -1 : x1 > x2 ? 1 : 0;
    }

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int
     * @see prefuse.util.collections.LiteralComparator#compare(long, long) */
    @Override
    public int compare(long x1, long x2) {
        return x1 < x2 ? -1 : x1 > x2 ? 1 : 0;
    }

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int
     * @see prefuse.util.collections.LiteralComparator#compare(float, float) */
    @Override
    public int compare(float x1, float x2) {
        return Float.compare(x1, x2);
    }

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int
     * @see prefuse.util.collections.LiteralComparator#compare(double, double) */
    @Override
    public int compare(double x1, double x2) {
        return Double.compare(x1, x2);
    }

    /** Compare.
     * 
     * @param x1
     *            the x1
     * @param x2
     *            the x2
     * @return the int
     * @see prefuse.util.collections.LiteralComparator#compare(boolean, boolean) */
    @Override
    public int compare(boolean x1, boolean x2) {
        return x1 ? x2 ? 0 : 1 : x2 ? -1 : 0;
    }

    /** Compare.
     * 
     * @param o1
     *            the o1
     * @param o2
     *            the o2
     * @return the int
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object) */
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null ? 0 : -1;
        } else if (o2 == null) {
            return 1;
        }
        if (o1 instanceof Comparable) {
            return ((Comparable) o1).compareTo(o2);
        } else if (o2 instanceof Comparable) {
            return -1 * ((Comparable) o2).compareTo(o1);
        } else if (o1 instanceof Boolean && o2 instanceof Boolean) {
            // unfortunate hack necessary for Java 1.4 compatibility
            return compare(((Boolean) o1).booleanValue(), ((Boolean) o2).booleanValue());
        } else {
            throw new IllegalArgumentException("Incomparable arguments.");
        }
    }
} // end of class DefaultLiteralComparator
