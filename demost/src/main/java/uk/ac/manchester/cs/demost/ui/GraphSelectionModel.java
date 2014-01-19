package uk.ac.manchester.cs.demost.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prefuse.visual.VisualItem;

/** The Class GraphSelectionModel. */
public class GraphSelectionModel {
    /** The listeners. */
    private final Set<GraphSelectionModelListener> listeners = new HashSet<GraphSelectionModelListener>();
    /** The selected items. */
    private final List<VisualItem> selectedItems = new ArrayList<VisualItem>();

    /** Adds the graph selection model listener.
     * 
     * @param l
     *            the l */
    public void addGraphSelectionModelListener(GraphSelectionModelListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }

    /** Removes the graph selection model listener.
     * 
     * @param l
     *            the l */
    public void removeGraphSelectionModelListener(GraphSelectionModelListener l) {
        listeners.remove(l);
    }

    /** Notify listeners.
     * 
     * @param event
     *            the event */
    private void notifyListeners(GraphSelectionEvent event) {
        for (GraphSelectionModelListener l : listeners) {
            l.selectionChanged(event);
        }
    }

    /** Gets the selected items.
     * 
     * @return the selectedItems */
    public Set<VisualItem> getSelectedItems() {
        return new HashSet<VisualItem>(selectedItems);
    }

    /** Sets the selected items.
     * 
     * @param selectedItems
     *            the selected items
     * @param source
     *            the source */
    public void setSelectedItems(Collection<? extends VisualItem> selectedItems,
            Object source) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
        notifyListeners(new GraphSelectionEvent(source, selectedItems));
    }
}
