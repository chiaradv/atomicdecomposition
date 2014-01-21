package prefuse.activity;

/** Adapter class for ActivityListeners. Provides empty implementations of
 * ActivityListener routines.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class ActivityAdapter implements ActivityListener {
    /** Activity scheduled.
     * 
     * @param a
     *            the a
     * @see prefuse.activity.ActivityListener#activityScheduled(prefuse.activity.Activity) */
    @Override
    public void activityScheduled(Activity a) {}

    /** Activity started.
     * 
     * @param a
     *            the a
     * @see prefuse.activity.ActivityListener#activityStarted(prefuse.activity.Activity) */
    @Override
    public void activityStarted(Activity a) {}

    /** Activity stepped.
     * 
     * @param a
     *            the a
     * @see prefuse.activity.ActivityListener#activityStepped(prefuse.activity.Activity) */
    @Override
    public void activityStepped(Activity a) {}

    /** Activity finished.
     * 
     * @param a
     *            the a
     * @see prefuse.activity.ActivityListener#activityFinished(prefuse.activity.Activity) */
    @Override
    public void activityFinished(Activity a) {}

    /** Activity cancelled.
     * 
     * @param a
     *            the a
     * @see prefuse.activity.ActivityListener#activityCancelled(prefuse.activity.Activity) */
    @Override
    public void activityCancelled(Activity a) {}
} // end of class ActivityAdapter
