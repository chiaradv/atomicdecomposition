/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefuse.txt" for licensing terms.
 */
package prefuse.util.collections;

import java.util.NoSuchElementException;

/** IntIterator implementation that provides an iteration over the contents of an
 * int array.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class IntArrayIterator extends IntIterator {
    /** The m_array. */
    private final int[] m_array;
    /** The m_cur. */
    private int m_cur;
    /** The m_end. */
    private final int m_end;

    /** Instantiates a new int array iterator.
     * 
     * @param array
     *            the array
     * @param start
     *            the start
     * @param len
     *            the len */
    public IntArrayIterator(int[] array, int start, int len) {
        m_array = array;
        m_cur = start;
        m_end = start + len;
    }

    /** Next int.
     * 
     * @return the int
     * @see prefuse.util.collections.IntIterator#nextInt() */
    @Override
    public int nextInt() {
        if (m_cur >= m_end) {
            throw new NoSuchElementException();
        }
        return m_array[m_cur++];
    }

    /** Checks for next.
     * 
     * @return true, if successful
     * @see java.util.Iterator#hasNext() */
    @Override
    public boolean hasNext() {
        return m_cur < m_end;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
} // end of class IntArrayIterator
