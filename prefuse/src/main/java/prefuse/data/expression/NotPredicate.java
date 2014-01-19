package prefuse.data.expression;

import prefuse.data.Tuple;


/**
 * Predicate representing the negation of another predicate.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NotPredicate extends AbstractPredicate {

    /** The m_predicate. */
    private Predicate m_predicate;
    
    /**
     * Create a new NotPredicate.
     * @param p the predicate to negate
     */
    public NotPredicate(Predicate p) {
        m_predicate = p;
    }
    
    /**
     * Get the negated predicate.
     * @return the negated predicate
     */
    public Predicate getPredicate() {
        return m_predicate;
    }
    
    /**
     * Gets the boolean.
     *
     * @param t the t
     * @return the boolean
     * @see prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        return !m_predicate.getBoolean(t);
    }

    /**
     * Visit.
     *
     * @param v the v
     * @see prefuse.data.expression.Expression#visit(prefuse.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
        v.down();
        m_predicate.visit(v);
        v.up();
    }
    
    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "NOT "+m_predicate.toString();
    }
    
    /**
     * Adds the child listeners.
     *
     * @see prefuse.data.expression.AbstractExpression#addChildListeners()
     */
    protected void addChildListeners() {
        m_predicate.addExpressionListener(this);
    }
    
    /**
     * Removes the child listeners.
     *
     * @see prefuse.data.expression.AbstractExpression#removeChildListeners()
     */
    protected void removeChildListeners() {
        m_predicate.removeExpressionListener(this);
    }
    
} // end of class NotPredicate