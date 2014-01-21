package prefuse.data.expression;

/** Abstract base class for Expression implementations that maintain two
 * sub-expressions. These are referred to as the left expression (the first one)
 * and the right expression (the second one).
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class BinaryExpression extends AbstractExpression {
    /** The m_op. */
    protected int m_op;
    /** The m_left. */
    protected Expression m_left;
    /** The m_right. */
    protected Expression m_right;

    /** Create a new BinaryExpression.
     * 
     * @param operation
     *            int value indicating the operation to be performed on the
     *            subtrees. The actual range of acceptable values are determined
     *            by subclasses.
     * @param minOp
     *            the minimum legal operation code value
     * @param maxOp
     *            the maximum legal operation code value
     * @param left
     *            the left sub-expression
     * @param right
     *            the right sub-expression */
    protected BinaryExpression(int operation, int minOp, int maxOp, Expression left,
            Expression right) {
        // operation check
        if (operation < minOp || operation > maxOp) {
            throw new IllegalArgumentException("Unknown operation type: " + operation);
        }
        // null check;
        if (left == null || right == null) {
            throw new IllegalArgumentException("Expressions must be non-null.");
        }
        m_op = operation;
        m_left = left;
        m_right = right;
    }

    /** Get the left sub-expression.
     * 
     * @return the left sub-expression */
    public Expression getLeftExpression() {
        return m_left;
    }

    /** Get the right sub-expression.
     * 
     * @return the right sub-expression */
    public Expression getRightExpression() {
        return m_right;
    }

    /** Set the left sub-expression. Any listeners will be notified.
     * 
     * @param e
     *            the left sub-expression to use */
    public void setLeftExpression(Expression e) {
        m_left.removeExpressionListener(this);
        m_left = e;
        if (hasListeners()) {
            e.addExpressionListener(this);
        }
        fireExpressionChange();
    }

    /** Set the right sub-expression. Any listeners will be notified.
     * 
     * @param e
     *            the right sub-expression to use */
    public void setRightExpression(Expression e) {
        m_right.removeExpressionListener(this);
        m_right = e;
        if (hasListeners()) {
            e.addExpressionListener(this);
        }
        fireExpressionChange();
    }

    /** Get the operation code for this expression. The meaning of this code is
     * left for subclasses to determine.
     * 
     * @return the operation code */
    public int getOperation() {
        return m_op;
    }

    /** Visit.
     * 
     * @param v
     *            the v
     * @see prefuse.data.expression.Expression#visit(prefuse.data.expression.ExpressionVisitor) */
    @Override
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
        v.down();
        m_left.visit(v);
        v.up();
        v.down();
        m_right.visit(v);
        v.up();
    }

    /** Adds the child listeners.
     * 
     * @see prefuse.data.expression.AbstractExpression#addChildListeners() */
    @Override
    protected void addChildListeners() {
        m_left.addExpressionListener(this);
        m_right.addExpressionListener(this);
    }

    /** Removes the child listeners.
     * 
     * @see prefuse.data.expression.AbstractExpression#removeChildListeners() */
    @Override
    protected void removeChildListeners() {
        m_left.removeExpressionListener(this);
        m_right.removeExpressionListener(this);
    }
} // end of abstract class BinaryExpression
