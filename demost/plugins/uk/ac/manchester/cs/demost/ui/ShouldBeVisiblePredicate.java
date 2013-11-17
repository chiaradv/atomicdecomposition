package uk.ac.manchester.cs.demost.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ListModel;

import org.semanticweb.owlapi.model.OWLObject;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.Predicate;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;

public class ShouldBeVisiblePredicate extends AbstractPredicate implements
		Predicate {
	private final ListModel listModel;
	private final AtomicDecomposition atomicDecomposition;
	private final Set<Atom> mainAtoms = new HashSet<Atom>();

	public ShouldBeVisiblePredicate(ListModel listModel,
			AtomicDecomposition atomicDecomposition) {
		if (listModel == null) {
			throw new NullPointerException("The list Model cannot be null");
		}
		this.atomicDecomposition = atomicDecomposition;
		this.listModel = listModel;
		this.reset();
	}

	/**
	 * @param listModel
	 * @param atomicDecomposition
	 */
	public void reset() {
		this.mainAtoms.clear();
		for (int i = 0; i < this.listModel.getSize(); i++) {
			OWLObject owlObject = (OWLObject) this.listModel.getElementAt(i);
			Set<Atom> atoms = this.atomicDecomposition.getAtoms();
			for (Atom atom : atoms) {
				if (atom.getSignature().contains(owlObject)) {
					this.mainAtoms.add(atom);
				}
			}
		}
	}

	@Override
	public boolean getBoolean(Tuple t) {
		boolean toReturn = false;
		if (t instanceof Edge) {
			Edge edge = (Edge) t;
			Node sourceNode = edge.getSourceNode();
			Node targetNode = edge.getTargetNode();
			toReturn = this.getBoolean(sourceNode)
					&& this.getBoolean(targetNode);
		} else {
			Atom atom = (Atom) t.get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME);
			toReturn = this.listModel.getSize() == 0
					|| this.isDependantFromMainAtoms(atom)
					|| this.anyOfMainAtomsDependsOn(atom);
		}
		return toReturn;
	}

	private boolean anyOfMainAtomsDependsOn(Atom atom) {
		boolean found = false;
		Iterator<Atom> iterator = this.mainAtoms.iterator();
		Set<Atom> dependencies = this.atomicDecomposition.getDependencies(atom);
		while (!found && iterator.hasNext()) {
			Atom aMainAtom = iterator.next();
			found = dependencies.contains(aMainAtom);
		}
		return found;
	}

	private boolean isDependantFromMainAtoms(Atom atom) {
		boolean found = false;
		Iterator<Atom> iterator = this.mainAtoms.iterator();
		while (!found && iterator.hasNext()) {
			Atom aMainAtom = iterator.next();
			found = this.atomicDecomposition.getDependencies(aMainAtom)
					.contains(atom);
		}
		return found;
	}
}
