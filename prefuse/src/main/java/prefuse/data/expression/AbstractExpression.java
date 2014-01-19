package prefuse.data.expression;

import prefuse.data.Tuple;
import prefuse.data.event.ExpressionListener;
import prefuse.util.collections.CopyOnWriteArrayList;

/** Abstract base class for Expression implementations. Provides support for
 * listeners and defaults every Expression evaluation method to an unsupported
 * operation.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractExpression implements Expression, ExpressionListener {
    /** The m_listeners. */
    private final CopyOnWriteArrayList m_listeners = new CopyOnWriteArrayList();

    /** Visit.
     * 
     * @param v
     *            the v
     * @see prefuse.data.expression.Expression#visit(prefuse.data.expression.ExpressionVisitor) */
    @Override
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
    }

    /** Adds the expression listener.
     * 
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.expression.Expression#addExpressionListener(prefuse.data.event.ExpressionListener) */
    @Override
    public final void addExpressionListener(ExpressionListener lstnr) {
        if (!m_listeners.contains(lstnr)) {
            m_listeners.add(lstnr);
            addChildListeners();
        }
    }

    /** Removes the expression listener.
     * 
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.expression.Expression#removeExpressionListener(prefuse.data.event.ExpressionListener) */
    @Override
    public final void removeExpressionListener(ExpressionListener lstnr) {
        m_listeners.remove(lstnr);
        if (m_listeners.size() == 0) {
            removeChildListeners();
        }
    }

    /** Indicates if any listeners are registered with this Expression.
     * 
     * @return true if listeners are registered, false otherwise */
    protected final boolean hasListeners() {
        return m_listeners != null && m_listeners.size() > 0;
    }

    /** Fire an expression change. */
    protected final void fireExpressionChange() {
        Object[] lstnrs = m_listeners.getArray();
        for (int i = 0; i < lstnrs.length; ++i) {
            ((ExpressionListener) lstnrs[i]).expressionChanged(this);
        }
    }

    /** Add child listeners to catch and propagate sub-expression updates. */
    protected void addChildListeners() {
        // nothing to do
    }

    /** Remove child listeners for sub-expression updates. */
    protected void removeChildListeners() {
        // nothing to do
    }

    /** Relay an expression change event.
     * 
     * @param expr
     *            the expr
     * @see prefuse.data.event.ExpressionListener#expressionChanged(prefuse.data.expression.Expression) */
    @Override
    public void expressionChanged(Expression expr) {
        fireExpressionChange();
    }

    // ------------------------------------------------------------------------
    // Default Implementation
    /** By default, throws an UnsupportedOperationException.
     * 
     * @param t
     *            the t
     * @return the object
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple) */
    @Override
    public Object get(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /** By default, throws an UnsupportedOperationException.
     * 
     * @param t
     *            the t
     * @return the int
     * @see prefuse.data.expression.Expression#getInt(prefuse.data.Tuple) */
    @Override
    public int getInt(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /** By default, throws an UnsupportedOperationException.
     * 
     * @param t
     *            the t
     * @return the long
     * @see prefuse.data.expression.Expression#getLong(prefuse.data.Tuple) */
    @Override
    public long getLong(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /** By default, throws an UnsupportedOperationException.
     * 
     * @param t
     *            the t
     * @return the float
     * @see prefuse.data.expression.Expression#getFloat(prefuse.data.Tuple) */
    @Override
    public float getFloat(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /** By default, throws an UnsupportedOperationException.
     * 
     * @param t
     *            the t
     * @return the double
     * @see prefuse.data.expression.Expression#getDouble(prefuse.data.Tuple) */
    @Override
    public double getDouble(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /** By default, throws an UnsupportedOperationException.
     * 
     * @param t
     *            the t
     * @return the boolean
     * @see prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple) */
    @Override
    public boolean getBoolean(Tuple t) {
        throw new UnsupportedOperationException();
    }
} // end of abstract class AbstractExpression
