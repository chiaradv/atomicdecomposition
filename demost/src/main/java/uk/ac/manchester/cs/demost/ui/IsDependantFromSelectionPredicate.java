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

/** The Class IsDependantFromSelectionPredicate. */
public class IsDependantFromSelectionPredicate extends AbstractPredicate implements
        Predicate {
    /** The graph selection model. */
    private final GraphSelectionModel graphSelectionModel;
    /** The atomic decomposition. */
    private final AtomicDecomposition atomicDecomposition;

    /** Instantiates a new checks if is dependant from selection predicate.
     * 
     * @param atomicDecomposition
     *            the atomic decomposition
     * @param graphSelectionModel
     *            the graph selection model */
    public IsDependantFromSelectionPredicate(AtomicDecomposition atomicDecomposition,
            GraphSelectionModel graphSelectionModel) {
        if (atomicDecomposition == null) {
            throw new NullPointerException("The atomic decomposition cannot be null");
        }
        this.graphSelectionModel = graphSelectionModel;
        this.atomicDecomposition = atomicDecomposition;
    }

    @Override
    public boolean getBoolean(Tuple t) {
        boolean toReturn = t instanceof Node;
        if (toReturn) {
            boolean found = false;
            Iterator<VisualItem> iterator = getGraphSelectionModel().getSelectedItems()
                    .iterator();
            while (!found && iterator.hasNext()) {
                VisualItem visualItem = iterator.next();
                Atom atom = (Atom) visualItem
                        .get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME);
                Set<Atom> descendants = getAtomicDecomposition().getDependencies(atom);
                found = descendants.contains(t
                        .get(DeMoStView.GENERATING_AXIOM_COLUMN_NAME));
            }
            toReturn = found;
        }
        return toReturn;
    }

    /** Gets the graph selection model.
     * 
     * @return the graphSelectionModel */
    public GraphSelectionModel getGraphSelectionModel() {
        return graphSelectionModel;
    }

    /** Gets the atomic decomposition.
     * 
     * @return the atomicDecomposition */
    public AtomicDecomposition getAtomicDecomposition() {
        return atomicDecomposition;
    }
}
