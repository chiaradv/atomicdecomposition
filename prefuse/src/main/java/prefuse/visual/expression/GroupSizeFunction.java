package prefuse.visual.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.VisualItem;


/**
 * GroupExpression that returns the size of a data group.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GroupSizeFunction extends GroupExpression {

    /**
     * Create a new, uninitialized GroupSizeFunction. The parameter for
     * this Function needs to be set.
     */
    public GroupSizeFunction() {
    }
    
    /**
     * Create a new GroupSizeFunction using the given data group name
     * as the Function parameter.
     * @param group the data group name to use as a parameter
     */
    public GroupSizeFunction(String group) {
        super(group);
    }
    
    /**
     * Gets the name.
     *
     * @return the name
     * @see prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "GROUPSIZE";
    }

    /**
     * Gets the type.
     *
     * @param s the s
     * @return the type
     * @see prefuse.data.expression.Expression#getType(prefuse.data.Schema)
     */
    public Class getType(Schema s) {
        return int.class;
    }

    /**
     * Gets the.
     *
     * @param t the t
     * @return the object
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple)
     */
    public Object get(Tuple t) {
        return new Integer(getInt(t));
    }

    /**
     * Gets the double.
     *
     * @param t the t
     * @return the double
     * @see prefuse.data.expression.Expression#getDouble(prefuse.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return getInt(t);
    }

    /**
     * Gets the float.
     *
     * @param t the t
     * @return the float
     * @see prefuse.data.expression.Expression#getFloat(prefuse.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return getInt(t);
    }

    /**
     * Gets the int.
     *
     * @param t the t
     * @return the int
     * @see prefuse.data.expression.Expression#getInt(prefuse.data.Tuple)
     */
    public int getInt(Tuple t) {
        String group = getGroup(t);
        if ( group == null ) { return -1; }
        TupleSet ts = ((VisualItem)t).getVisualization().getGroup(group);
        return ( ts==null ? 0 : ts.getTupleCount() );
    }

    /**
     * Gets the long.
     *
     * @param t the t
     * @return the long
     * @see prefuse.data.expression.Expression#getLong(prefuse.data.Tuple)
     */
    public long getLong(Tuple t) {
        return getInt(t);
    }

} // end of class GroupSizeFunction
