package prefuse.util.force;

/** Represents a constant gravitational force, like the pull of gravity for an
 * object on the Earth (F = mg). The force experienced by a given item is
 * calculated as the product of a GravitationalConstant parameter and the mass
 * of the item.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class GravitationalForce extends AbstractForce {
    /** The Constant pnames. */
    private static final String[] pnames = { "GravitationalConstant", "Direction" };
    /** The Constant GRAVITATIONAL_CONST. */
    public static final int GRAVITATIONAL_CONST = 0;
    /** The Constant DIRECTION. */
    public static final int DIRECTION = 1;
    /** The Constant DEFAULT_FORCE_CONSTANT. */
    public static final float DEFAULT_FORCE_CONSTANT = 1E-4f;
    /** The Constant DEFAULT_MIN_FORCE_CONSTANT. */
    public static final float DEFAULT_MIN_FORCE_CONSTANT = 1E-5f;
    /** The Constant DEFAULT_MAX_FORCE_CONSTANT. */
    public static final float DEFAULT_MAX_FORCE_CONSTANT = 1E-3f;
    /** The Constant DEFAULT_DIRECTION. */
    public static final float DEFAULT_DIRECTION = (float) -Math.PI / 2;
    /** The Constant DEFAULT_MIN_DIRECTION. */
    public static final float DEFAULT_MIN_DIRECTION = (float) -Math.PI;
    /** The Constant DEFAULT_MAX_DIRECTION. */
    public static final float DEFAULT_MAX_DIRECTION = (float) Math.PI;

    /** Create a new GravitationForce.
     * 
     * @param forceConstant
     *            the gravitational constant to use
     * @param direction
     *            the direction in which gravity should act, in radians. */
    public GravitationalForce(float forceConstant, float direction) {
        params = new float[] { forceConstant, direction };
        minValues = new float[] { DEFAULT_MIN_FORCE_CONSTANT, DEFAULT_MIN_DIRECTION };
        maxValues = new float[] { DEFAULT_MAX_FORCE_CONSTANT, DEFAULT_MAX_DIRECTION };
    }

    /** Create a new GravitationalForce with default gravitational constant and
     * direction. */
    public GravitationalForce() {
        this(DEFAULT_FORCE_CONSTANT, DEFAULT_DIRECTION);
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
        float theta = params[DIRECTION];
        float coeff = params[GRAVITATIONAL_CONST] * item.mass;
        item.force[0] += Math.cos(theta) * coeff;
        item.force[1] += Math.sin(theta) * coeff;
    }
} // end of class GravitationalForce
