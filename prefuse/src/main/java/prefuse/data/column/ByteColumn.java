package prefuse.data.column;

import java.util.Arrays;

import prefuse.data.DataReadOnlyException;
import prefuse.data.DataTypeException;

/** Column implementation for storing int values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class ByteColumn extends AbstractColumn {
    /** The m_values. */
    private byte[] m_values;
    /** The m_size. */
    private int m_size;

    /** Create a new empty IntColumn. */
    public ByteColumn() {
        this(0, 10, (byte) 0);
    }

    /** Create a new IntColumn.
     * 
     * @param nrows
     *            the initial size of the column */
    public ByteColumn(int nrows) {
        this(nrows, nrows, (byte) 0);
    }

    /** Create a new IntColumn.
     * 
     * @param nrows
     *            the initial size of the column
     * @param capacity
     *            the initial capacity of the column
     * @param defaultValue
     *            the default value for the column */
    public ByteColumn(int nrows, int capacity, byte defaultValue) {
        super(byte.class, new Byte(defaultValue));
        if (capacity < nrows) {
            throw new IllegalArgumentException(
                    "Capacity value can not be less than the row count.");
        }
        m_values = new byte[capacity];
        Arrays.fill(m_values, defaultValue);
        m_size = nrows;
    }

    // ------------------------------------------------------------------------
    // Column Metadata
    /** Gets the row count.
     * 
     * @return the row count
     * @see prefuse.data.column.Column#getRowCount() */
    @Override
    public int getRowCount() {
        return m_size;
    }

    /** Sets the maximum row.
     * 
     * @param nrows
     *            the new maximum row
     * @see prefuse.data.column.Column#setMaximumRow(int) */
    @Override
    public void setMaximumRow(int nrows) {
        if (nrows > m_values.length) {
            int capacity = Math.max(3 * m_values.length / 2 + 1, nrows);
            byte[] values = new byte[capacity];
            System.arraycopy(m_values, 0, values, 0, m_size);
            Arrays.fill(values, m_size, capacity, ((Byte) m_defaultValue).byteValue());
            m_values = values;
        }
        m_size = nrows;
    }

    // ------------------------------------------------------------------------
    // Data Access Methods
    /** Gets the.
     * 
     * @param row
     *            the row
     * @return the object
     * @see prefuse.data.column.Column#get(int) */
    @Override
    public Object get(int row) {
        return new Byte(getByte(row));
    }

    /** Sets the.
     * 
     * @param val
     *            the val
     * @param row
     *            the row
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.Column#set(java.lang.Object, int) */
    @Override
    public void set(Object val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (val != null) {
            if (val instanceof Number) {
                setInt(((Number) val).byteValue(), row);
            } else if (val instanceof String) {
                setString((String) val, row);
            } else {
                throw new DataTypeException(val.getClass());
            }
        } else {
            throw new DataTypeException("Column does not accept null values");
        }
    }

    // ------------------------------------------------------------------------
    // Data Type Convenience Methods
    /** Gets the byte.
     * 
     * @param row
     *            the row
     * @return the byte
     * @throws DataTypeException
     *             the data type exception */
    public byte getByte(int row) throws DataTypeException {
        if (row < 0 || row > m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        return m_values[row];
    }

    /** Gets the int.
     * 
     * @param row
     *            the row
     * @return the int
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.AbstractColumn#getInt(int) */
    @Override
    public int getInt(int row) throws DataTypeException {
        if (row < 0 || row > m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        return m_values[row];
    }

    /** Sets the int.
     * 
     * @param val
     *            the val
     * @param row
     *            the row
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.AbstractColumn#setInt(int, int) */
    @Override
    public void setInt(int val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (row < 0 || row >= m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        // get the previous value
        int prev = m_values[row];
        // exit early if no change
        if (prev == val) {
            return;
        }
        // set the new value
        m_values[row] = (byte) val;
        // fire a change event
        fireColumnEvent(row, prev);
    }

    // ------------------------------------------------------------------------
    /** Gets the long.
     * 
     * @param row
     *            the row
     * @return the long
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.Column#getLong(int) */
    @Override
    public long getLong(int row) throws DataTypeException {
        return getInt(row);
    }

    /** Gets the float.
     * 
     * @param row
     *            the row
     * @return the float
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.Column#getFloat(int) */
    @Override
    public float getFloat(int row) throws DataTypeException {
        return getInt(row);
    }

    /** Gets the double.
     * 
     * @param row
     *            the row
     * @return the double
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.Column#getDouble(int) */
    @Override
    public double getDouble(int row) throws DataTypeException {
        return getInt(row);
    }
} // end of class IntColumn
