package prefuse.data.tuple;

import java.util.Date;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

/** Tuple implementation that pulls values from a backing data Table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class TableTuple implements Tuple {
    /** The m_table. */
    protected Table m_table;
    /** The m_row. */
    protected int m_row;

    /** Initialize a new TableTuple for the given table and row. Tuples are
     * automatically generated by {@link TupleManager} instances, and so
     * application code should never need to invoke this method.
     * 
     * @param table
     *            the data Table
     * @param graph
     *            ignored by this class
     * @param row
     *            the table row index */
    protected void init(Table table, Graph graph, int row) {
        m_table = table;
        m_row = m_table.isValidRow(row) ? row : -1;
    }

    /** Gets the schema.
     * 
     * @return the schema
     * @see prefuse.data.Tuple#getSchema() */
    @Override
    public Schema getSchema() {
        return m_table.getSchema();
    }

    /** Gets the table.
     * 
     * @return the table
     * @see prefuse.data.Tuple#getTable() */
    @Override
    public Table getTable() {
        return m_table;
    }

    /** Gets the row.
     * 
     * @return the row
     * @see prefuse.data.Tuple#getRow() */
    @Override
    public int getRow() {
        return m_row;
    }

    // ------------------------------------------------------------------------
    // Index Checking
    /** Checks if is valid.
     * 
     * @return true, if is valid
     * @see prefuse.data.Tuple#isValid() */
    @Override
    public boolean isValid() {
        return m_row != -1;
    }

    /** Invalidates this tuple. Called by an enclosing table when a row is
     * deleted. */
    void invalidate() {
        m_row = -1;
    }

    /** Internal validity check. Throw an exception if the tuple is not valid. */
    private void validityCheck() {
        if (m_row == -1) {
            throw new IllegalStateException("This tuple is no longer valid. "
                    + "It has been deleted from its table");
        }
    }

    // ------------------------------------------------------------------------
    // Column Methods
    /** Gets the column type.
     * 
     * @param field
     *            the field
     * @return the column type
     * @see prefuse.data.Tuple#getColumnType(java.lang.String) */
    @Override
    public Class getColumnType(String field) {
        return m_table.getColumnType(field);
    }

    /** Gets the column type.
     * 
     * @param col
     *            the col
     * @return the column type
     * @see prefuse.data.Tuple#getColumnType(int) */
    @Override
    public Class getColumnType(int col) {
        return m_table.getColumnType(col);
    }

    /** Gets the column index.
     * 
     * @param field
     *            the field
     * @return the column index
     * @see prefuse.data.Tuple#getColumnIndex(java.lang.String) */
    @Override
    public int getColumnIndex(String field) {
        return m_table.getColumnNumber(field);
    }

    /** Gets the column count.
     * 
     * @return the column count
     * @see prefuse.data.Tuple#getColumnCount() */
    @Override
    public int getColumnCount() {
        return m_table.getColumnCount();
    }

    /** Gets the column name.
     * 
     * @param col
     *            the col
     * @return the column name
     * @see prefuse.data.Tuple#getColumnName(int) */
    @Override
    public String getColumnName(int col) {
        return m_table.getColumnName(col);
    }

    // ------------------------------------------------------------------------
    // Data Access Methods
    /** Can get.
     * 
     * @param field
     *            the field
     * @param type
     *            the type
     * @return true, if successful
     * @see prefuse.data.Tuple#canGet(java.lang.String, java.lang.Class) */
    @Override
    public boolean canGet(String field, Class type) {
        return m_table.canGet(field, type);
    }

    /** Can set.
     * 
     * @param field
     *            the field
     * @param type
     *            the type
     * @return true, if successful
     * @see prefuse.data.Tuple#canSet(java.lang.String, java.lang.Class) */
    @Override
    public boolean canSet(String field, Class type) {
        return m_table.canSet(field, type);
    }

    /** Gets the.
     * 
     * @param field
     *            the field
     * @return the object
     * @see prefuse.data.Tuple#get(java.lang.String) */
    @Override
    public final Object get(String field) {
        validityCheck();
        return m_table.get(m_row, field);
    }

    /** Sets the.
     * 
     * @param field
     *            the field
     * @param value
     *            the value
     * @see prefuse.data.Tuple#set(java.lang.String, java.lang.Object) */
    @Override
    public final void set(String field, Object value) {
        validityCheck();
        m_table.set(m_row, field, value);
    }

    /** Gets the.
     * 
     * @param idx
     *            the idx
     * @return the object
     * @see prefuse.data.Tuple#get(int) */
    @Override
    public final Object get(int idx) {
        validityCheck();
        return m_table.get(m_row, idx);
    }

    /** Sets the.
     * 
     * @param idx
     *            the idx
     * @param value
     *            the value
     * @see prefuse.data.Tuple#set(int, java.lang.Object) */
    @Override
    public final void set(int idx, Object value) {
        validityCheck();
        m_table.set(m_row, idx, value);
    }

    /** Gets the default.
     * 
     * @param field
     *            the field
     * @return the default
     * @see prefuse.data.Tuple#getDefault(java.lang.String) */
    @Override
    public Object getDefault(String field) {
        validityCheck();
        return m_table.getDefault(field);
    }

    /** Revert to default.
     * 
     * @param field
     *            the field
     * @see prefuse.data.Tuple#revertToDefault(java.lang.String) */
    @Override
    public void revertToDefault(String field) {
        validityCheck();
        m_table.revertToDefault(m_row, field);
    }

    // ------------------------------------------------------------------------
    // Convenience Data Access Methods
    /** Can get int.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetInt(java.lang.String) */
    @Override
    public final boolean canGetInt(String field) {
        return m_table.canGetInt(field);
    }

    /** Can set int.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetInt(java.lang.String) */
    @Override
    public final boolean canSetInt(String field) {
        return m_table.canSetInt(field);
    }

    /** Gets the int.
     * 
     * @param field
     *            the field
     * @return the int
     * @see prefuse.data.Tuple#getInt(java.lang.String) */
    @Override
    public final int getInt(String field) {
        validityCheck();
        return m_table.getInt(m_row, field);
    }

    /** Sets the int.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setInt(java.lang.String, int) */
    @Override
    public final void setInt(String field, int val) {
        validityCheck();
        m_table.setInt(m_row, field, val);
    }

    /** Gets the int.
     * 
     * @param col
     *            the col
     * @return the int
     * @see prefuse.data.Tuple#getInt(int) */
    @Override
    public final int getInt(int col) {
        validityCheck();
        return m_table.getInt(m_row, col);
    }

    /** Sets the int.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setInt(int, int) */
    @Override
    public final void setInt(int col, int val) {
        validityCheck();
        m_table.setInt(m_row, col, val);
    }

    // --------------------------------------------------------------
    /** Can get long.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetLong(java.lang.String) */
    @Override
    public final boolean canGetLong(String field) {
        return m_table.canGetLong(field);
    }

    /** Can set long.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetLong(java.lang.String) */
    @Override
    public final boolean canSetLong(String field) {
        return m_table.canSetLong(field);
    }

    /** Gets the long.
     * 
     * @param field
     *            the field
     * @return the long
     * @see prefuse.data.Tuple#getLong(java.lang.String) */
    @Override
    public final long getLong(String field) {
        validityCheck();
        return m_table.getLong(m_row, field);
    }

    /** Sets the long.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setLong(java.lang.String, long) */
    @Override
    public final void setLong(String field, long val) {
        validityCheck();
        m_table.setLong(m_row, field, val);
    }

    /** Gets the long.
     * 
     * @param col
     *            the col
     * @return the long
     * @see prefuse.data.Tuple#getLong(int) */
    @Override
    public final long getLong(int col) {
        validityCheck();
        return m_table.getLong(m_row, col);
    }

    /** Sets the long.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setLong(int, long) */
    @Override
    public final void setLong(int col, long val) {
        validityCheck();
        m_table.setLong(m_row, col, val);
    }

    // --------------------------------------------------------------
    /** Can get float.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetFloat(java.lang.String) */
    @Override
    public final boolean canGetFloat(String field) {
        return m_table.canGetFloat(field);
    }

    /** Can set float.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetFloat(java.lang.String) */
    @Override
    public final boolean canSetFloat(String field) {
        return m_table.canSetFloat(field);
    }

    /** Gets the float.
     * 
     * @param field
     *            the field
     * @return the float
     * @see prefuse.data.Tuple#getFloat(java.lang.String) */
    @Override
    public final float getFloat(String field) {
        validityCheck();
        return m_table.getFloat(m_row, field);
    }

    /** Sets the float.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setFloat(java.lang.String, float) */
    @Override
    public final void setFloat(String field, float val) {
        validityCheck();
        m_table.setFloat(m_row, field, val);
    }

    /** Gets the float.
     * 
     * @param col
     *            the col
     * @return the float
     * @see prefuse.data.Tuple#getFloat(int) */
    @Override
    public final float getFloat(int col) {
        validityCheck();
        return m_table.getFloat(m_row, col);
    }

    /** Sets the float.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setFloat(int, float) */
    @Override
    public final void setFloat(int col, float val) {
        validityCheck();
        m_table.setFloat(m_row, col, val);
    }

    // --------------------------------------------------------------
    /** Can get double.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetDouble(java.lang.String) */
    @Override
    public final boolean canGetDouble(String field) {
        return m_table.canGetDouble(field);
    }

    /** Can set double.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetDouble(java.lang.String) */
    @Override
    public final boolean canSetDouble(String field) {
        return m_table.canSetDouble(field);
    }

    /** Gets the double.
     * 
     * @param field
     *            the field
     * @return the double
     * @see prefuse.data.Tuple#getDouble(java.lang.String) */
    @Override
    public final double getDouble(String field) {
        validityCheck();
        return m_table.getDouble(m_row, field);
    }

    /** Sets the double.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setDouble(java.lang.String, double) */
    @Override
    public final void setDouble(String field, double val) {
        validityCheck();
        m_table.setDouble(m_row, field, val);
    }

    /** Gets the double.
     * 
     * @param col
     *            the col
     * @return the double
     * @see prefuse.data.Tuple#getDouble(int) */
    @Override
    public final double getDouble(int col) {
        validityCheck();
        return m_table.getDouble(m_row, col);
    }

    /** Sets the double.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setDouble(int, double) */
    @Override
    public final void setDouble(int col, double val) {
        validityCheck();
        m_table.setDouble(m_row, col, val);
    }

    // --------------------------------------------------------------
    /** Can get boolean.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetBoolean(java.lang.String) */
    @Override
    public final boolean canGetBoolean(String field) {
        return m_table.canGetBoolean(field);
    }

    /** Can set boolean.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetBoolean(java.lang.String) */
    @Override
    public final boolean canSetBoolean(String field) {
        return m_table.canSetBoolean(field);
    }

    /** Gets the boolean.
     * 
     * @param field
     *            the field
     * @return the boolean
     * @see prefuse.data.Tuple#getBoolean(java.lang.String) */
    @Override
    public final boolean getBoolean(String field) {
        validityCheck();
        return m_table.getBoolean(m_row, field);
    }

    /** Sets the boolean.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setBoolean(java.lang.String, boolean) */
    @Override
    public final void setBoolean(String field, boolean val) {
        validityCheck();
        m_table.setBoolean(m_row, field, val);
    }

    /** Gets the boolean.
     * 
     * @param col
     *            the col
     * @return the boolean
     * @see prefuse.data.Tuple#getBoolean(int) */
    @Override
    public final boolean getBoolean(int col) {
        validityCheck();
        return m_table.getBoolean(m_row, col);
    }

    /** Sets the boolean.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setBoolean(java.lang.String, boolean) */
    @Override
    public final void setBoolean(int col, boolean val) {
        validityCheck();
        m_table.setBoolean(m_row, col, val);
    }

    // --------------------------------------------------------------
    /** Can get string.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetString(java.lang.String) */
    @Override
    public final boolean canGetString(String field) {
        return m_table.canGetString(field);
    }

    /** Can set string.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetString(java.lang.String) */
    @Override
    public final boolean canSetString(String field) {
        return m_table.canSetString(field);
    }

    /** Gets the string.
     * 
     * @param field
     *            the field
     * @return the string
     * @see prefuse.data.Tuple#getString(java.lang.String) */
    @Override
    public final String getString(String field) {
        validityCheck();
        return m_table.getString(m_row, field);
    }

    /** Sets the string.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setString(java.lang.String, java.lang.String) */
    @Override
    public final void setString(String field, String val) {
        validityCheck();
        m_table.setString(m_row, field, val);
    }

    /** Gets the string.
     * 
     * @param col
     *            the col
     * @return the string
     * @see prefuse.data.Tuple#getString(int) */
    @Override
    public final String getString(int col) {
        validityCheck();
        return m_table.getString(m_row, col);
    }

    /** Sets the string.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setString(int, java.lang.String) */
    @Override
    public final void setString(int col, String val) {
        validityCheck();
        m_table.setString(m_row, col, val);
    }

    // --------------------------------------------------------------
    /** Can get date.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canGetDate(java.lang.String) */
    @Override
    public final boolean canGetDate(String field) {
        return m_table.canGetDate(field);
    }

    /** Can set date.
     * 
     * @param field
     *            the field
     * @return true, if successful
     * @see prefuse.data.Tuple#canSetDate(java.lang.String) */
    @Override
    public final boolean canSetDate(String field) {
        return m_table.canSetDate(field);
    }

    /** Gets the date.
     * 
     * @param field
     *            the field
     * @return the date
     * @see prefuse.data.Tuple#getDate(java.lang.String) */
    @Override
    public final Date getDate(String field) {
        validityCheck();
        return m_table.getDate(m_row, field);
    }

    /** Sets the date.
     * 
     * @param field
     *            the field
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setDate(java.lang.String, java.util.Date) */
    @Override
    public final void setDate(String field, Date val) {
        validityCheck();
        m_table.setDate(m_row, field, val);
    }

    /** Gets the date.
     * 
     * @param col
     *            the col
     * @return the date
     * @see prefuse.data.Tuple#getDate(int) */
    @Override
    public final Date getDate(int col) {
        validityCheck();
        return m_table.getDate(m_row, col);
    }

    /** Sets the date.
     * 
     * @param col
     *            the col
     * @param val
     *            the val
     * @see prefuse.data.Tuple#setDate(java.lang.String, java.util.Date) */
    @Override
    public final void setDate(int col, Date val) {
        validityCheck();
        m_table.setDate(m_row, col, val);
    }

    // ------------------------------------------------------------------------
    /** To string.
     * 
     * @return the string
     * @see java.lang.Object#toString() */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Tuple[");
        for (int i = 0; i < getColumnCount(); ++i) {
            if (i > 0) {
                sb.append(',');
            }
            try {
                sb.append(get(i).toString());
            } catch (Exception e) {
                sb.append("?");
            }
        }
        sb.append("]");
        return sb.toString();
    }
} // end of class TableTuple
