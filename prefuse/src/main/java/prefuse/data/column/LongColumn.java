package prefuse.data.column;

import java.util.Arrays;

import prefuse.data.DataReadOnlyException;
import prefuse.data.DataTypeException;

/** Column implementation for storing long values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class LongColumn extends AbstractColumn {
    /** The m_values. */
    private long[] m_values;
    /** The m_size. */
    private int m_size;

    /** Create a new empty LongColumn. */
    public LongColumn() {
        this(0, 10, 0L);
    }

    /** Create a new LongColumn.
     * 
     * @param nrows
     *            the initial size of the column */
    public LongColumn(int nrows) {
        this(nrows, nrows, 0L);
    }

    /** Create a new LongColumn.
     * 
     * @param nrows
     *            the initial size of the column
     * @param capacity
     *            the initial capacity of the column
     * @param defaultValue
     *            the default value for the column */
    public LongColumn(int nrows, int capacity, long defaultValue) {
        super(long.class, new Long(defaultValue));
        if (capacity < nrows) {
            throw new IllegalArgumentException(
                    "Capacity value can not be less than the row count.");
        }
        m_values = new long[capacity];
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
            long[] values = new long[capacity];
            System.arraycopy(m_values, 0, values, 0, m_size);
            Arrays.fill(values, m_size, capacity, ((Long) m_defaultValue).longValue());
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
        return new Long(getLong(row));
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
                setLong(((Number) val).longValue(), row);
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
    /** Gets the long.
     * 
     * @param row
     *            the row
     * @return the long
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.AbstractColumn#getLong(int) */
    @Override
    public long getLong(int row) throws DataTypeException {
        if (row < 0 || row > m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        return m_values[row];
    }

    /** Sets the long.
     * 
     * @param val
     *            the val
     * @param row
     *            the row
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.AbstractColumn#setLong(long, int) */
    @Override
    public void setLong(long val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (row < 0 || row >= m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        // get the previous value
        long prev = m_values[row];
        // exit early if no change
        if (prev == val) {
            return;
        }
        // set the new value
        m_values[row] = val;
        // fire a change event
        fireColumnEvent(row, prev);
    }

    // /**
    // * @see prefuse.data.column.AbstractColumn#getString(int)
    // */
    // public String getString(int row) throws DataTypeException {
    // return String.valueOf(getLong(row));
    // }
    //
    // /**
    // * @see prefuse.data.column.AbstractColumn#setString(java.lang.String,
    // int)
    // */
    // public void setString(String val, int row) throws DataTypeException {
    // setLong(Long.parseLong(val), row);
    // }
    // ------------------------------------------------------------------------
    /** Gets the int.
     * 
     * @param row
     *            the row
     * @return the int
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.Column#getInt(int) */
    @Override
    public int getInt(int row) throws DataTypeException {
        return (int) getLong(row);
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
        return getLong(row);
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
        return getLong(row);
    }
} // end of class LongColumn
