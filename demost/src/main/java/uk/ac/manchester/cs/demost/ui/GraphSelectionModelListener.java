package uk.ac.manchester.cs.demost.ui;

/** The listener interface for receiving graphSelectionModel events. The class
 * that is interested in processing a graphSelectionModel event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's {@code addGraphSelectionModelListener} method.
 * When the graphSelectionModel event occurs, that object's appropriate method
 * is invoked.
 * 
 * @see GraphSelectionEvent */
public interface GraphSelectionModelListener {
    /** Selection changed.
     * 
     * @param graphSelectionEvent
     *            the graph selection event */
    public void selectionChanged(GraphSelectionEvent graphSelectionEvent);
}
