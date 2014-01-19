package prefuse.data.util;

import prefuse.data.event.ProjectionListener;
import prefuse.util.collections.CopyOnWriteArrayList;

/** Abstract base class for column projection instances. Implements the listener
 * functionality.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractColumnProjection implements ColumnProjection {
    // ------------------------------------------------------------------------
    // Listener Methods
    /** The m_listeners. */
    private CopyOnWriteArrayList m_listeners;

    /** Adds the projection listener.
     * 
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.util.ColumnProjection#addProjectionListener(prefuse.data.event.ProjectionListener) */
    @Override
    public void addProjectionListener(ProjectionListener lstnr) {
        if (m_listeners == null) {
            m_listeners = new CopyOnWriteArrayList();
        }
        if (!m_listeners.contains(lstnr)) {
            m_listeners.add(lstnr);
        }
    }

    /** Removes the projection listener.
     * 
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.util.ColumnProjection#removeProjectionListener(prefuse.data.event.ProjectionListener) */
    @Override
    public void removeProjectionListener(ProjectionListener lstnr) {
        if (m_listeners != null) {
            m_listeners.remove(lstnr);
        }
        if (m_listeners.size() == 0) {
            m_listeners = null;
        }
    }

    /** Fire update. */
    public void fireUpdate() {
        if (m_listeners == null) {
            return;
        }
        Object[] lstnrs = m_listeners.getArray();
        for (int i = 0; i < lstnrs.length; ++i) {
            ((ProjectionListener) lstnrs[i]).projectionChanged(this);
        }
    }
} // end of abstract class AbstractColumnProjection
