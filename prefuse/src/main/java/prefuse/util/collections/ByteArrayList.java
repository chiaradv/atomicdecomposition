package prefuse.util.collections;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** A resizable array that maintains a list of byte values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class ByteArrayList {
    /** The m_bytes. */
    private byte[] m_bytes;
    /** The m_size. */
    private int m_size;

    /** Instantiates a new byte array list. */
    public ByteArrayList() {
        this(4096);
    }

    /** Instantiates a new byte array list.
     * 
     * @param capacity
     *            the capacity */
    public ByteArrayList(int capacity) {
        m_bytes = new byte[capacity];
        m_size = 0;
    }

    /** Range check.
     * 
     * @param i
     *            the i */
    private void rangeCheck(int i) {
        if (i < 0 || i >= m_size) {
            throw new IndexOutOfBoundsException("Index: " + i + " Size: " + m_size);
        }
    }

    /** Ensure capacity.
     * 
     * @param cap
     *            the cap */
    private void ensureCapacity(int cap) {
        if (m_bytes.length < cap) {
            int capacity = Math.max(3 * m_bytes.length / 2 + 1, cap);
            byte[] nbytes = new byte[capacity];
            System.arraycopy(m_bytes, 0, nbytes, 0, m_size);
            m_bytes = nbytes;
        }
    }

    /** Gets the.
     * 
     * @param i
     *            the i
     * @return the byte */
    public byte get(int i) {
        rangeCheck(i);
        return m_bytes[i];
    }

    /** Sets the.
     * 
     * @param i
     *            the i
     * @param b
     *            the b */
    public void set(int i, byte b) {
        rangeCheck(i);
        m_bytes[i] = b;
    }

    /** Size.
     * 
     * @return the int */
    public int size() {
        return m_size;
    }

    /** Adds the.
     * 
     * @param b
     *            the b */
    public void add(byte b) {
        ensureCapacity(m_size + 1);
        m_bytes[m_size++] = b;
    }

    /** Adds the.
     * 
     * @param b
     *            the b
     * @param start
     *            the start
     * @param len
     *            the len */
    public void add(byte[] b, int start, int len) {
        ensureCapacity(m_size + len);
        System.arraycopy(b, start, m_bytes, m_size, len);
        m_size += len;
    }

    /** Gets the as input stream.
     * 
     * @return the as input stream */
    public InputStream getAsInputStream() {
        return new ByteArrayInputStream(m_bytes, 0, m_size);
    }

    /** To array.
     * 
     * @return the byte[] */
    public byte[] toArray() {
        byte[] b = new byte[m_size];
        System.arraycopy(m_bytes, 0, b, 0, m_size);
        return b;
    }
}
