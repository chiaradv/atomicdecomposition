package prefuse.data.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;

/** Expression instance that returns the value stored in a Tuple data field.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class ColumnExpression extends AbstractExpression implements Predicate {
    /** The m_field. */
    protected final String m_field;

    /** Create a new ColumnExpression.
     * 
     * @param field
     *            the column / data field name to use */
    public ColumnExpression(String field) {
        m_field = field;
    }

    /** Get the column / data field name used by this expression.
     * 
     * @return the column / data field name */
    public String getColumnName() {
        return m_field;
    }

    // ------------------------------------------------------------------------
    // Expression Interface
    /** Gets the type.
     * 
     * @param s
     *            the s
     * @return the type
     * @see prefuse.data.expression.Expression#getType(prefuse.data.Schema) */
    @Override
    public Class getType(Schema s) {
        return s.getColumnType(m_field);
    }

    /** Gets the.
     * 
     * @param t
     *            the t
     * @return the object
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple) */
    @Override
    public Object get(Tuple t) {
        return t.get(m_field);
    }

    /** Gets the int.
     * 
     * @param t
     *            the t
     * @return the int
     * @see prefuse.data.expression.Expression#getInt(prefuse.data.Tuple) */
    @Override
    public int getInt(Tuple t) {
        return t.getInt(m_field);
    }

    /** Gets the long.
     * 
     * @param t
     *            the t
     * @return the long
     * @see prefuse.data.expression.Expression#getLong(prefuse.data.Tuple) */
    @Override
    public long getLong(Tuple t) {
        return t.getLong(m_field);
    }

    /** Gets the float.
     * 
     * @param t
     *            the t
     * @return the float
     * @see prefuse.data.expression.Expression#getFloat(prefuse.data.Tuple) */
    @Override
    public float getFloat(Tuple t) {
        return t.getFloat(m_field);
    }

    /** Gets the double.
     * 
     * @param t
     *            the t
     * @return the double
     * @see prefuse.data.expression.Expression#getDouble(prefuse.data.Tuple) */
    @Override
    public double getDouble(Tuple t) {
        return t.getDouble(m_field);
    }

    /** Gets the boolean.
     * 
     * @param t
     *            the t
     * @return the boolean
     * @see prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple) */
    @Override
    public boolean getBoolean(Tuple t) {
        return t.getBoolean(m_field);
    }

    /** To string.
     * 
     * @return the string
     * @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "[" + m_field + "]";
    }
} // end of class ColumnExpression
