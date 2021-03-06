package prefuse.util.force;

/** Uses a gravitational force model to act as a circular "wall". Can be used to
 * construct circles which either attract or repel items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class CircularWallForce extends AbstractForce {
    /** The pnames. */
    private static String[] pnames = new String[] { "GravitationalConstant" };
    /** The Constant DEFAULT_GRAV_CONSTANT. */
    public static final float DEFAULT_GRAV_CONSTANT = -0.1f;
    /** The Constant DEFAULT_MIN_GRAV_CONSTANT. */
    public static final float DEFAULT_MIN_GRAV_CONSTANT = -1.0f;
    /** The Constant DEFAULT_MAX_GRAV_CONSTANT. */
    public static final float DEFAULT_MAX_GRAV_CONSTANT = 1.0f;
    /** The Constant GRAVITATIONAL_CONST. */
    public static final int GRAVITATIONAL_CONST = 0;
    /** The r. */
    private final float x, y, r;

    /** Create a new CircularWallForce.
     * 
     * @param gravConst
     *            the gravitational constant to use
     * @param x
     *            the center x-coordinate of the circle
     * @param y
     *            the center y-coordinate of the circle
     * @param r
     *            the radius of the circle */
    public CircularWallForce(float gravConst, float x, float y, float r) {
        params = new float[] { gravConst };
        minValues = new float[] { DEFAULT_MIN_GRAV_CONSTANT };
        maxValues = new float[] { DEFAULT_MAX_GRAV_CONSTANT };
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /** Create a new CircularWallForce with default gravitational constant.
     * 
     * @param x
     *            the center x-coordinate of the circle
     * @param y
     *            the center y-coordinate of the circle
     * @param r
     *            the radius of the circle */
    public CircularWallForce(float x, float y, float r) {
        this(DEFAULT_GRAV_CONSTANT, x, y, r);
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
        float[] n = item.location;
        float dx = x - n[0];
        float dy = y - n[1];
        float d = (float) Math.sqrt(dx * dx + dy * dy);
        float dr = r - d;
        float c = dr > 0 ? -1 : 1;
        float v = c * params[GRAVITATIONAL_CONST] * item.mass / (dr * dr);
        if (d == 0.0) {
            dx = ((float) Math.random() - 0.5f) / 50.0f;
            dy = ((float) Math.random() - 0.5f) / 50.0f;
            d = (float) Math.sqrt(dx * dx + dy * dy);
        }
        item.force[0] += v * dx / d;
        item.force[1] += v * dy / d;
        // System.out.println(dx/d+","+dy/d+","+dr+","+v);
    }
} // end of class CircularWallForce
