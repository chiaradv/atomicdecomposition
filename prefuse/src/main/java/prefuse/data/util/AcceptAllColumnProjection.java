package prefuse.data.util;

import prefuse.data.column.Column;

/** ColumnProjection that simply includes all columns.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class AcceptAllColumnProjection extends AbstractColumnProjection {
    /** Always returns true, accepting all columns.
     * 
     * @param col
     *            the col
     * @param name
     *            the name
     * @return true, if successful
     * @see prefuse.data.util.ColumnProjection#include(prefuse.data.column.Column,
     *      java.lang.String) */
    @Override
    public boolean include(Column col, String name) {
        return true;
    }
} // end of class AcceptAllColumnProjection
