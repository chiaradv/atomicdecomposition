package uk.ac.manchester.cs.demost.ui;

import java.util.Enumeration;
import java.util.Set;

import javax.swing.DefaultListModel;

import org.protege.editor.core.ui.list.MList;
import org.semanticweb.owlapi.model.OWLEntity;

import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.Predicate;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;

public class IsSelectedInListPredicate extends AbstractPredicate implements
		Predicate {
	private final DefaultListModel selectedEntitiesListModel;
	private final MList selectedEntitiesList;
	private final AtomicDecomposition atomicDecomposition;

	public IsSelectedInListPredicate(AtomicDecomposition atomicDecomposition,
			DefaultListModel selectedEntitiesListModel,
			MList selectedEntitiesList) {
		if (atomicDecomposition == null) {
			throw new NullPointerException(
					"The atomic decomposition cannot be null");
		}
		this.selectedEntitiesListModel = selectedEntitiesListModel;
		this.atomicDecomposition = atomicDecomposition;
		this.selectedEntitiesList = selectedEntitiesList;
	}

	@Override
	public boolean getBoolean(Tuple t) {
		boolean toReturn = t instanceof Node;
		if (toReturn) {
			if (selectedEntitiesList.isSelectionEmpty()) {
				// then it's all elements in the list model
				if (selectedEntitiesListModel.isEmpty()) {
					// default to not highlight anything
					return false;
				}
				Atom atom = (Atom) t
						.get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME);
				final Set<OWLEntity> signature = (Set<OWLEntity>) atom
						.getSignature();
				Enumeration<?> en = selectedEntitiesListModel.elements();
				while (en.hasMoreElements()) {
					if (signature.contains(en.nextElement())) {
						return true;
					}
				}
				return false;
			} else {
				Atom atom = (Atom) t
						.get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME);
				final Set<OWLEntity> signature = (Set<OWLEntity>) atom
						.getSignature();
				try {
					for (Object o : selectedEntitiesList.getSelectedValues()) {
						if (signature.contains(o)) {
							return true;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					return false;
				}
				return false;
			}
		}
		return toReturn;
	}

	/**
	 * @return the atomicDecomposition
	 */
	public AtomicDecomposition getAtomicDecomposition() {
		return this.atomicDecomposition;
	}
}
