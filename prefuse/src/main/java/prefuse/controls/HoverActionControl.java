package prefuse.controls;

import java.awt.event.MouseEvent;

import prefuse.visual.VisualItem;

/** Control that executes an action when the mouse passes over an item.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class HoverActionControl extends ControlAdapter {
    /** The m_action. */
    private final String m_action;

    /** Create a new HoverActionControl.
     * 
     * @param action
     *            the action to run upon mouse-over. The action is run both upon
     *            entering and upon exiting the item. */
    public HoverActionControl(String action) {
        m_action = action;
    }

    /** Item entered.
     * 
     * @param item
     *            the item
     * @param e
     *            the e
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem,
     *      java.awt.event.MouseEvent) */
    @Override
    public void itemEntered(VisualItem item, MouseEvent e) {
        item.getVisualization().run(m_action);
    }

    /** Item exited.
     * 
     * @param item
     *            the item
     * @param e
     *            the e
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem,
     *      java.awt.event.MouseEvent) */
    @Override
    public void itemExited(VisualItem item, MouseEvent e) {
        item.getVisualization().run(m_action);
    }
} // end of class HoverActionControl
