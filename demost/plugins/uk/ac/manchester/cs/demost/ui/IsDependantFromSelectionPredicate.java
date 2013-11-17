package uk.ac.manchester.cs.demost.ui;

import java.util.Iterator;
import java.util.Set;

import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.Predicate;
import prefuse.visual.VisualItem;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;

public class IsDependantFromSelectionPredicate extends AbstractPredicate
		implements Predicate {
	private final GraphSelectionModel graphSelectionModel;
	private final AtomicDecomposition atomicDecomposition;

	public IsDependantFromSelectionPredicate(
			AtomicDecomposition atomicDecomposition,
			GraphSelectionModel graphSelectionModel) {
		if (atomicDecomposition == null) {
			throw new NullPointerException(
					"The atomic decomposition cannot be null");
		}
		this.graphSelectionModel = graphSelectionModel;
		this.atomicDecomposition = atomicDecomposition;
	}

	@Override
	public boolean getBoolean(Tuple t) {
		boolean toReturn = t instanceof Node;
		if (toReturn) {
			boolean found = false;
			Iterator<VisualItem> iterator = this.getGraphSelectionModel()
					.getSelectedItems().iterator();
			while (!found && iterator.hasNext()) {
				VisualItem visualItem = iterator.next();
				Atom atom = (Atom) visualItem
						.get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME);
				Set<Atom> descendants = this.getAtomicDecomposition()
						.getDependencies(atom);
				found = descendants.contains(t
						.get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME));
			}
			toReturn = found;
		}
		return toReturn;
	}

	/**
	 * @return the graphSelectionModel
	 */
	public GraphSelectionModel getGraphSelectionModel() {
		return this.graphSelectionModel;
	}

	/**
	 * @return the atomicDecomposition
	 */
	public AtomicDecomposition getAtomicDecomposition() {
		return this.atomicDecomposition;
	}
}
