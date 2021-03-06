package prefuse.action.animate;

import prefuse.action.ItemAction;
import prefuse.visual.VisualItem;

/** Animator that linearly interpolates the size of a VisualItems.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class SizeAnimator extends ItemAction {
    /** Create a new SizeAnimator that processes all data groups. */
    public SizeAnimator() {
        super();
    }

    /** Create a new SizeAnimator that processes the specified group.
     * 
     * @param group
     *            the data group to process. */
    public SizeAnimator(String group) {
        super(group);
    }

    /** Process.
     * 
     * @param item
     *            the item
     * @param frac
     *            the frac
     * @see prefuse.action.ItemAction#process(prefuse.visual.VisualItem, double) */
    @Override
    public void process(VisualItem item, double frac) {
        double ss = item.getStartSize();
        item.setSize(ss + frac * (item.getEndSize() - ss));
    }
} // end of class SizeAnimator
