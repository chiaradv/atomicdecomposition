package prefuse.data.column;

import java.util.Arrays;

import prefuse.data.DataReadOnlyException;
import prefuse.data.DataTypeException;

/** Column instance for sotring flaot values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class FloatColumn extends AbstractColumn {
    /** The m_values. */
    private float[] m_values;
    /** The m_size. */
    private int m_size;

    /** Create a new empty FloatColumn. */
    public FloatColumn() {
        this(0, 10, 0f);
    }

    /** Create a new FloatColumn.
     * 
     * @param nrows
     *            the initial size of the column */
    public FloatColumn(int nrows) {
        this(nrows, nrows, 0f);
    }

    /** Create a new FloatColumn.
     * 
     * @param nrows
     *            the initial size of the column
     * @param capacity
     *            the initial capacity of the column
     * @param defaultValue
     *            the default value for the column */
    public FloatColumn(int nrows, int capacity, float defaultValue) {
        super(float.class, new Float(defaultValue));
        if (capacity < nrows) {
            throw new IllegalArgumentException(
                    "Capacity value can not be less than the row count.");
        }
        m_values = new float[capacity];
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
            float[] values = new float[capacity];
            System.arraycopy(m_values, 0, values, 0, m_size);
            Arrays.fill(values, m_size, capacity, ((Float) m_defaultValue).floatValue());
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
        return new Float(getFloat(row));
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
                setFloat(((Number) val).floatValue(), row);
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
    /** Gets the float.
     * 
     * @param row
     *            the row
     * @return the float
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.AbstractColumn#getFloat(int) */
    @Override
    public float getFloat(int row) throws DataTypeException {
        if (row < 0 || row > m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        return m_values[row];
    }

    /** Sets the float.
     * 
     * @param val
     *            the val
     * @param row
     *            the row
     * @throws DataTypeException
     *             the data type exception
     * @see prefuse.data.column.AbstractColumn#setFloat(float, int) */
    @Override
    public void setFloat(float val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (row < 0 || row >= m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        // get the previous value
        float prev = m_values[row];
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
    // return String.valueOf(getFloat(row));
    // }
    //
    // /**
    // * @see prefuse.data.column.AbstractColumn#setString(java.lang.String,
    // int)
    // */
    // public void setString(String val, int row) throws DataTypeException {
    // setFloat(Float.parseFloat(val), row);
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
        return (int) getFloat(row);
    }

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
        return (long) getFloat(row);
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
        return getFloat(row);
    }
} // end of class FloatColumn
