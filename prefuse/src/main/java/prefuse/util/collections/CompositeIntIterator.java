package prefuse.util.collections;

import java.util.NoSuchElementException;

/** IntIterator implementation that combines the results of multiple int
 * iterators.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class CompositeIntIterator extends IntIterator {
    /** The m_iters. */
    private IntIterator[] m_iters;
    /** The m_cur. */
    private int m_cur;

    /** Instantiates a new composite int iterator.
     * 
     * @param iter1
     *            the iter1
     * @param iter2
     *            the iter2 */
    public CompositeIntIterator(IntIterator iter1, IntIterator iter2) {
        this(new IntIterator[] { iter1, iter2 });
    }

    /** Instantiates a new composite int iterator.
     * 
     * @param iters
     *            the iters */
    public CompositeIntIterator(IntIterator[] iters) {
        m_iters = iters;
        m_cur = 0;
    }

    /** Next int.
     * 
     * @return the int
     * @see prefuse.util.collections.IntIterator#nextInt() */
    @Override
    public int nextInt() {
        if (hasNext()) {
            return m_iters[m_cur].nextInt();
        } else {
            throw new NoSuchElementException();
        }
    }

    /** Checks for next.
     * 
     * @return true, if successful
     * @see java.util.Iterator#hasNext() */
    @Override
    public boolean hasNext() {
        if (m_iters == null) {
            return false;
        }
        while (true) {
            if (m_iters[m_cur].hasNext()) {
                return true;
            } else if (++m_cur >= m_iters.length) {
                m_iters = null;
                return false;
            }
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
} // end of class CompositeIntIterator
