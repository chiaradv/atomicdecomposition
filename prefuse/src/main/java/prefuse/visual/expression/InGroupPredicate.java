package prefuse.visual.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;
import prefuse.visual.VisualItem;

/** Expression that indicates if an item is currently a member of a particular
 * data group. The data group name is provided by a String-valued
 * sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class InGroupPredicate extends GroupExpression implements Predicate {
    /** Create a new InGroupPredicate. */
    public InGroupPredicate() {}

    /** Create a new InGroupPredicate.
     * 
     * @param group
     *            the data group name to use as a parameter */
    public InGroupPredicate(String group) {
        super(group);
    }

    /** Gets the.
     * 
     * @param t
     *            the t
     * @return the object
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple) */
    @Override
    public Object get(Tuple t) {
        return getBoolean(t) ? Boolean.TRUE : Boolean.FALSE;
    }

    /** Gets the boolean.
     * 
     * @param t
     *            the t
     * @return the boolean
     * @see prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple) */
    @Override
    public boolean getBoolean(Tuple t) {
        if (!(t instanceof VisualItem)) {
            return false;
        }
        String group = getGroup(t);
        if (group == null) {
            return false;
        }
        VisualItem item = (VisualItem) t;
        return item.getVisualization().isInGroup(item, group);
    }

    /** Gets the name.
     * 
     * @return the name
     * @see prefuse.data.expression.Function#getName() */
    @Override
    public String getName() {
        return "INGROUP";
    }

    /** Gets the type.
     * 
     * @param s
     *            the s
     * @return the type
     * @see prefuse.data.expression.Expression#getType(prefuse.data.Schema) */
    @Override
    public Class getType(Schema s) {
        return boolean.class;
    }
} // end of class InGroupPredicate
