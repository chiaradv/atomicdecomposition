package prefuse.util.force;

/** Abstract base class for force functions in a force simulation. This skeletal
 * version provides support for storing and retrieving float-valued parameters
 * of the force function. Subclasses should use the protected field
 * <code>params</code> to store parameter values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractForce implements Force {
    /** The params. */
    protected float[] params;
    /** The min values. */
    protected float[] minValues;
    /** The max values. */
    protected float[] maxValues;

    /** Initialize this force function. This default implementation does nothing.
     * Subclasses should override this method with any needed initialization.
     * 
     * @param fsim
     *            the encompassing ForceSimulator */
    @Override
    public void init(ForceSimulator fsim) {
        // do nothing.
    }

    /** Gets the parameter count.
     * 
     * @return the parameter count
     * @see prefuse.util.force.Force#getParameterCount() */
    @Override
    public int getParameterCount() {
        return params == null ? 0 : params.length;
    }

    /** Gets the parameter.
     * 
     * @param i
     *            the i
     * @return the parameter
     * @see prefuse.util.force.Force#getParameter(int) */
    @Override
    public float getParameter(int i) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return params[i];
        }
    }

    /** Gets the min value.
     * 
     * @param i
     *            the i
     * @return the min value
     * @see prefuse.util.force.Force#getMinValue(int) */
    @Override
    public float getMinValue(int i) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return minValues[i];
        }
    }

    /** Gets the max value.
     * 
     * @param i
     *            the i
     * @return the max value
     * @see prefuse.util.force.Force#getMaxValue(int) */
    @Override
    public float getMaxValue(int i) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return maxValues[i];
        }
    }

    /** Gets the parameter name.
     * 
     * @param i
     *            the i
     * @return the parameter name
     * @see prefuse.util.force.Force#getParameterName(int) */
    @Override
    public String getParameterName(int i) {
        String[] pnames = getParameterNames();
        if (i < 0 || pnames == null || i >= pnames.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return pnames[i];
        }
    }

    /** Sets the parameter.
     * 
     * @param i
     *            the i
     * @param val
     *            the val
     * @see prefuse.util.force.Force#setParameter(int, float) */
    @Override
    public void setParameter(int i, float val) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            params[i] = val;
        }
    }

    /** Sets the min value.
     * 
     * @param i
     *            the i
     * @param val
     *            the val
     * @see prefuse.util.force.Force#setMinValue(int, float) */
    @Override
    public void setMinValue(int i, float val) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            minValues[i] = val;
        }
    }

    /** Sets the max value.
     * 
     * @param i
     *            the i
     * @param val
     *            the val
     * @see prefuse.util.force.Force#setMaxValue(int, float) */
    @Override
    public void setMaxValue(int i, float val) {
        if (i < 0 || params == null || i >= params.length) {
            throw new IndexOutOfBoundsException();
        } else {
            maxValues[i] = val;
        }
    }

    /** Gets the parameter names.
     * 
     * @return the parameter names */
    protected abstract String[] getParameterNames();

    /** Returns false.
     * 
     * @return true, if is item force
     * @see prefuse.util.force.Force#isItemForce() */
    @Override
    public boolean isItemForce() {
        return false;
    }

    /** Returns false.
     * 
     * @return true, if is spring force
     * @see prefuse.util.force.Force#isSpringForce() */
    @Override
    public boolean isSpringForce() {
        return false;
    }

    /** Throws an UnsupportedOperationException.
     * 
     * @param item
     *            the item
     * @see prefuse.util.force.Force#getForce(prefuse.util.force.ForceItem) */
    @Override
    public void getForce(ForceItem item) {
        throw new UnsupportedOperationException(
                "This class does not support this operation");
    }

    /** Throws an UnsupportedOperationException.
     * 
     * @param spring
     *            the spring
     * @see prefuse.util.force.Force#getForce(prefuse.util.force.Spring) */
    @Override
    public void getForce(Spring spring) {
        throw new UnsupportedOperationException(
                "This class does not support this operation");
    }
} // end of abstract class AbstractForce
