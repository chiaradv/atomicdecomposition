package uk.ac.manchester.cs.demost.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prefuse.visual.VisualItem;

public class GraphSelectionEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7964245710073066407L;
	private final List<VisualItem> selected = new ArrayList<VisualItem>();

	/**
	 * @param source
	 */
	public GraphSelectionEvent(Object source, Collection<? extends VisualItem> selected) {
		super(source);
		if (selected == null) {
			throw new NullPointerException("The collection of selected items cannot be null");
		}
		this.selected.addAll(selected);
	}

	/**
	 * @return the selected
	 */
	public Set<VisualItem> getSelected() {
		return new HashSet<VisualItem>(this.selected);
	}
}
