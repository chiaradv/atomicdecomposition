package prefuse.data.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.util.TypeLib;


/**
 * Expression instance representing an "if then else" clause in the prefuse
 * expression language.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class IfExpression extends AbstractExpression {

    /** The m_test. */
    private Predicate m_test;
    
    /** The m_then. */
    private Expression m_then;
    
    /** The m_else. */
    private Expression m_else;
    
    /**
     * Create a new IfExpression.
     * @param test the predicate test for the if statement
     * @param thenExpr the expression to evaluate if the test predicate
     * evaluates to true
     * @param elseExpr the expression to evaluate if the test predicate
     * evaluates to false
     */
    public IfExpression(Predicate test,
            Expression thenExpr, Expression elseExpr)
    {
        m_test = test;
        m_then = thenExpr;
        m_else = elseExpr;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Get the test predicate.
     * @return the test predicate
     */
    public Predicate getTestPredicate() {
        return m_test;
    }

    /**
     * Get the then expression.
     *
     * @return the then expression
     */
    public Expression getThenExpression() {
        return m_then;
    }

    /**
     * Get the else expression.
     *
     * @return the else expression
     */
    public Expression getElseExpression() {
        return m_else;
    }
    
    /**
     * Set the test predicate.
     * @param p the test predicate
     */
    public void setTestPredicate(Predicate p) {
        m_test.removeExpressionListener(this);
        m_test = p;
        if ( hasListeners() ) p.addExpressionListener(this);
        fireExpressionChange();
    }

    /**
     * Set the then expression.
     *
     * @param e the then expression to set
     */
    public void setThenExpression(Expression e) {
        m_then.removeExpressionListener(this);
        m_then = e;
        if ( hasListeners() ) e.addExpressionListener(this);
        fireExpressionChange();
    }

    /**
     * Set the else expression.
     *
     * @param e the else expression to set
     */
    public void setElseExpression(Expression e) {
        m_else.removeExpressionListener(this);
        m_else = e;
        if ( hasListeners() ) e.addExpressionListener(this);
        fireExpressionChange();
    }
    
    // ------------------------------------------------------------------------    
    
    /**
     * Gets the type.
     *
     * @param s the s
     * @return the type
     * @see prefuse.data.expression.Expression#getType(prefuse.data.Schema)
     */
    public Class getType(Schema s) {
        Class type1 = m_then.getType(s);
        Class type2 = m_else.getType(s);
        return TypeLib.getSharedType(type1, type2);
    }

    /**
     * Gets the.
     *
     * @param t the t
     * @return the object
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple)
     */
    public Object get(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).get(t);
    }

    /**
     * Gets the boolean.
     *
     * @param t the t
     * @return the boolean
     * @see prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getBoolean(t);
    }

    /**
     * Gets the double.
     *
     * @param t the t
     * @return the double
     * @see prefuse.data.expression.Expression#getDouble(prefuse.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getDouble(t);
    }

    /**
     * Gets the float.
     *
     * @param t the t
     * @return the float
     * @see prefuse.data.expression.Expression#getFloat(prefuse.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getFloat(t);
    }

    /**
     * Gets the int.
     *
     * @param t the t
     * @return the int
     * @see prefuse.data.expression.Expression#getInt(prefuse.data.Tuple)
     */
    public int getInt(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getInt(t);
    }

    /**
     * Gets the long.
     *
     * @param t the t
     * @return the long
     * @see prefuse.data.expression.Expression#getLong(prefuse.data.Tuple)
     */
    public long getLong(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getLong(t);
    }

    // ------------------------------------------------------------------------
    
    /**
     * Visit.
     *
     * @param v the v
     * @see prefuse.data.expression.Expression#visit(prefuse.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
        v.down(); m_test.visit(v); v.up();
        v.down(); m_then.visit(v); v.up();
        v.down(); m_else.visit(v); v.up();
    }
    
    /**
     * Adds the child listeners.
     *
     * @see prefuse.data.expression.AbstractExpression#addChildListeners()
     */
    protected void addChildListeners() {
        m_test.addExpressionListener(this);
        m_then.addExpressionListener(this);
        m_else.addExpressionListener(this);
    }
    
    /**
     * Removes the child listeners.
     *
     * @see prefuse.data.expression.AbstractExpression#removeChildListeners()
     */
    protected void removeChildListeners() {
        m_test.removeExpressionListener(this);
        m_then.removeExpressionListener(this);
        m_else.removeExpressionListener(this);
    }
    
    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "IF " + m_test.toString()
            + " THEN " + m_then.toString()
            + " ELSE " + m_else.toString();
    }
    
} // end of class IfExpression
