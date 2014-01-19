package prefuse.visual.expression;

import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Function;
import prefuse.data.expression.NotPredicate;
import prefuse.data.expression.Predicate;
import prefuse.visual.VisualItem;


/**
 * Expression that indicates if an item is currently under the mouse
 * pointer.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class HoverPredicate extends ColumnExpression
    implements Predicate, Function
{
    /** Convenience instance for the hover == true case. */
    public static final Predicate TRUE = new HoverPredicate();
    /** Convenience instance for the hover == false case. */
    public static final Predicate FALSE = new NotPredicate(TRUE);
    
    /**
     * Create a new HoverPredicate.
     */
    public HoverPredicate() {
        super(VisualItem.HOVER);
    }

    /**
     * Gets the name.
     *
     * @return the name
     * @see prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "HOVER";
    }

    /**
     * Adds the parameter.
     *
     * @param e the e
     * @see prefuse.data.expression.Function#addParameter(prefuse.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        throw new IllegalStateException("This function takes 0 parameters");
    }

    /**
     * Gets the parameter count.
     *
     * @return the parameter count
     * @see prefuse.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 0;
    }
    
    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"()";
    }

} // end of class HoverPredicate
