package uk.ac.manchester.cs.demost.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prefuse.visual.VisualItem;

public class GraphSelectionModel {
	private final Set<GraphSelectionModelListener> listeners = new HashSet<GraphSelectionModelListener>();
	private final List<VisualItem> selectedItems = new ArrayList<VisualItem>();

	public void addGraphSelectionModelListener(GraphSelectionModelListener l) {
		if (l != null) {
			this.listeners.add(l);
		}
	}

	public void removeGraphSelectionModelListener(GraphSelectionModelListener l) {
		this.listeners.remove(l);
	}

	private void notifyListeners(GraphSelectionEvent event) {
		for (GraphSelectionModelListener l : this.listeners) {
			l.selectionChanged(event);
		}
	}

	/**
	 * @return the selectedItems
	 */
	public Set<VisualItem> getSelectedItems() {
		return new HashSet<VisualItem>(this.selectedItems);
	}

	public void setSelectedItems(Collection<? extends VisualItem> selectedItems, Object source) {
		this.selectedItems.clear();
		this.selectedItems.addAll(selectedItems);
		this.notifyListeners(new GraphSelectionEvent(source, selectedItems));
	}
}
