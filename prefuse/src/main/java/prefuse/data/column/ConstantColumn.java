package prefuse.data.column;

import prefuse.data.DataTypeException;
import prefuse.data.event.ColumnListener;

/** Column implementation holding a single, constant value for all rows.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class ConstantColumn extends AbstractColumn {
    /** The m_size. */
    private int m_size;

    /** Create a new ConstantColumn.
     * 
     * @param type
     *            the data type of this column
     * @param defaultValue
     *            the default value used for all rows */
    public ConstantColumn(Class type, Object defaultValue) {
        super(type, defaultValue);
    }

    /** Gets the row count.
     * 
     * @return the row count
     * @see prefuse.data.column.Column#getRowCount() */
    @Override
    public int getRowCount() {
        return m_size + 1;
    }

    /** Sets the maximum row.
     * 
     * @param nrows
     *            the new maximum row
     * @see prefuse.data.column.Column#setMaximumRow(int) */
    @Override
    public void setMaximumRow(int nrows) {
        m_size = nrows;
    }

    /** Gets the.
     * 
     * @param row
     *            the row
     * @return the object
     * @see prefuse.data.column.Column#get(int) */
    @Override
    public Object get(int row) {
        if (row < 0 || row > m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        return super.m_defaultValue;
    }

    /** Unsupported operation.
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
        throw new UnsupportedOperationException("Can't set values on a ConstantColumn");
    }

    /** Returns false.
     * 
     * @param type
     *            the type
     * @return true, if successful
     * @see prefuse.data.column.Column#canSet(java.lang.Class) */
    @Override
    public boolean canSet(Class type) {
        return false;
    }

    /** Does nothing.
     * 
     * @param listener
     *            the listener
     * @see prefuse.data.column.Column#addColumnListener(prefuse.data.event.ColumnListener) */
    @Override
    public void addColumnListener(ColumnListener listener) {
        return; // column can't change, so nothing to listen to
    }

    /** Does nothing.
     * 
     * @param listener
     *            the listener
     * @see prefuse.data.column.Column#removeColumnListener(prefuse.data.event.ColumnListener) */
    @Override
    public void removeColumnListener(ColumnListener listener) {
        return; // column can't change, so nothing to listen to
    }
} // end of class Constant Column
