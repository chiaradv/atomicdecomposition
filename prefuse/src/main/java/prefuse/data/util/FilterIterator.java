package prefuse.data.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;

/** Iterator over tuples that filters the output by a given predicate.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class FilterIterator implements Iterator {
    /** The predicate. */
    private final Predicate predicate;
    /** The tuples. */
    private Iterator tuples;
    /** The next. */
    private Tuple next;

    /** Create a new FilterIterator.
     * 
     * @param tuples
     *            an iterator over tuples
     * @param p
     *            the filter predicate to use */
    public FilterIterator(Iterator tuples, Predicate p) {
        predicate = p;
        this.tuples = tuples;
        next = advance();
    }

    /** Advance.
     * 
     * @return the tuple */
    private Tuple advance() {
        while (tuples.hasNext()) {
            Tuple t = (Tuple) tuples.next();
            if (predicate.getBoolean(t)) {
                return t;
            }
        }
        tuples = null;
        next = null;
        return null;
    }

    /** Next.
     * 
     * @return the object
     * @see java.util.Iterator#next() */
    @Override
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        Tuple retval = next;
        next = advance();
        return retval;
    }

    /** Checks for next.
     * 
     * @return true, if successful
     * @see java.util.Iterator#hasNext() */
    @Override
    public boolean hasNext() {
        return tuples != null;
    }

    /** Not supported.
     * 
     * @see java.util.Iterator#remove() */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
} // end of class FilterIterator
