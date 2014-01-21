package prefuse.visual.expression;

import prefuse.Visualization;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.search.SearchTupleSet;
import prefuse.visual.VisualItem;

/** Expression that returns the current query string of a data group of the type.
 * {@link prefuse.data.search.SearchTupleSet}. The data group name is provided
 * by a String-valued sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class QueryExpression extends GroupExpression {
    /** Create a new QueryExpression. */
    public QueryExpression() {
        super();
    }

    /** Create a new QueryExpression.
     * 
     * @param group
     *            the data group name to use as a parameter */
    public QueryExpression(String group) {
        super(group);
    }

    /** Gets the name.
     * 
     * @return the name
     * @see prefuse.data.expression.Function#getName() */
    @Override
    public String getName() {
        return "QUERY";
    }

    /** Gets the type.
     * 
     * @param s
     *            the s
     * @return the type
     * @see prefuse.data.expression.Expression#getType(prefuse.data.Schema) */
    @Override
    public Class getType(Schema s) {
        return String.class;
    }

    /** Gets the.
     * 
     * @param t
     *            the t
     * @return the object
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple) */
    @Override
    public Object get(Tuple t) {
        VisualItem item = (VisualItem) t;
        Visualization vis = item.getVisualization();
        String group = getGroup(t);
        SearchTupleSet sts = (SearchTupleSet) vis.getGroup(group);
        return sts.getQuery();
    }
} // end of class QueryExpression
