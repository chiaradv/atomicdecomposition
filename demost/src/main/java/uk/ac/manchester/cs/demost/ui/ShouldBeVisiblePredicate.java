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

/** The Class ShouldBeVisiblePredicate. */
public class ShouldBeVisiblePredicate extends AbstractPredicate implements Predicate {
    /** The list model. */
    private final ListModel listModel;
    /** The atomic decomposition. */
    private final AtomicDecomposition atomicDecomposition;
    /** The main atoms. */
    private final Set<Atom> mainAtoms = new HashSet<Atom>();

    /** Instantiates a new should be visible predicate.
     * 
     * @param listModel
     *            the list model
     * @param atomicDecomposition
     *            the atomic decomposition */
    public ShouldBeVisiblePredicate(ListModel listModel,
            AtomicDecomposition atomicDecomposition) {
        if (listModel == null) {
            throw new NullPointerException("The list Model cannot be null");
        }
        this.atomicDecomposition = atomicDecomposition;
        this.listModel = listModel;
        reset();
    }

    /** Reset. */
    public void reset() {
        mainAtoms.clear();
        for (int i = 0; i < listModel.getSize(); i++) {
            OWLObject owlObject = (OWLObject) listModel.getElementAt(i);
            Set<Atom> atoms = atomicDecomposition.getAtoms();
            for (Atom atom : atoms) {
                if (atom.getSignature().contains(owlObject)) {
                    mainAtoms.add(atom);
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
            toReturn = getBoolean(sourceNode) && getBoolean(targetNode);
        } else {
            Atom atom = (Atom) t.get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME);
            toReturn = listModel.getSize() == 0 || isDependantFromMainAtoms(atom)
                    || anyOfMainAtomsDependsOn(atom);
        }
        return toReturn;
    }

    /** Any of main atoms depends on.
     * 
     * @param atom
     *            the atom
     * @return true, if successful */
    private boolean anyOfMainAtomsDependsOn(Atom atom) {
        boolean found = false;
        Iterator<Atom> iterator = mainAtoms.iterator();
        Set<Atom> dependencies = atomicDecomposition.getDependencies(atom);
        while (!found && iterator.hasNext()) {
            Atom aMainAtom = iterator.next();
            found = dependencies.contains(aMainAtom);
        }
        return found;
    }

    /** Checks if is dependant from main atoms.
     * 
     * @param atom
     *            the atom
     * @return true, if is dependant from main atoms */
    private boolean isDependantFromMainAtoms(Atom atom) {
        boolean found = false;
        Iterator<Atom> iterator = mainAtoms.iterator();
        while (!found && iterator.hasNext()) {
            Atom aMainAtom = iterator.next();
            found = atomicDecomposition.getDependencies(aMainAtom).contains(atom);
        }
        return found;
    }
}
