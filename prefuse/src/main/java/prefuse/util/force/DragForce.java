package prefuse.util.force;

/** Implements a viscosity/drag force to help stabilize items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class DragForce extends AbstractForce {
    /** The pnames. */
    private static String[] pnames = new String[] { "DragCoefficient" };
    /** The Constant DEFAULT_DRAG_COEFF. */
    public static final float DEFAULT_DRAG_COEFF = 0.01f;
    /** The Constant DEFAULT_MIN_DRAG_COEFF. */
    public static final float DEFAULT_MIN_DRAG_COEFF = 0.0f;
    /** The Constant DEFAULT_MAX_DRAG_COEFF. */
    public static final float DEFAULT_MAX_DRAG_COEFF = 0.1f;
    /** The Constant DRAG_COEFF. */
    public static final int DRAG_COEFF = 0;

    /** Create a new DragForce.
     * 
     * @param dragCoeff
     *            the drag co-efficient */
    public DragForce(float dragCoeff) {
        params = new float[] { dragCoeff };
        minValues = new float[] { DEFAULT_MIN_DRAG_COEFF };
        maxValues = new float[] { DEFAULT_MAX_DRAG_COEFF };
    }

    /** Create a new DragForce with default drag co-efficient. */
    public DragForce() {
        this(DEFAULT_DRAG_COEFF);
    }

    /** Returns true.
     * 
     * @return true, if is item force
     * @see prefuse.util.force.Force#isItemForce() */
    @Override
    public boolean isItemForce() {
        return true;
    }

    /** Gets the parameter names.
     * 
     * @return the parameter names
     * @see prefuse.util.force.AbstractForce#getParameterNames() */
    @Override
    protected String[] getParameterNames() {
        return pnames;
    }

    /** Gets the force.
     * 
     * @param item
     *            the item
     * @see prefuse.util.force.Force#getForce(prefuse.util.force.ForceItem) */
    @Override
    public void getForce(ForceItem item) {
        item.force[0] -= params[DRAG_COEFF] * item.velocity[0];
        item.force[1] -= params[DRAG_COEFF] * item.velocity[1];
    }
} // end of class DragForce
