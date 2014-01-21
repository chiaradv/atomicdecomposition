package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposerOWLAPITOOLS;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;
import uk.ac.manchester.cs.demost.ui.DeMoStGUI;

@SuppressWarnings("javadoc")
public class AtomicDecomposerDepedenciesTest {
    public static void main(String[] args) throws OWLOntologyCreationException {
        atomicDecomposerDepedenciesTest();
    }

    @Test
    public static void atomicDecomposerDepedenciesTest()
            throws OWLOntologyCreationException {
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology o = m.createOntology();
        OWLDataFactory f = m.getOWLDataFactory();
        OWLClass powerYoga = f.getOWLClass(IRI.create("urn:test#PowerYoga"));
        OWLClass yoga = f.getOWLClass(IRI.create("urn:test#Yoga"));
        OWLClass relaxation = f.getOWLClass(IRI.create("urn:test#Relaxation"));
        OWLClass activity = f.getOWLClass(IRI.create("urn:test#Activity"));
        OWLSubClassOfAxiom poweryogaSub = f.getOWLSubClassOfAxiom(powerYoga, yoga);
        OWLSubClassOfAxiom yogaSubRelax = f.getOWLSubClassOfAxiom(yoga, relaxation);
        OWLSubClassOfAxiom relaxSubActivity = f.getOWLSubClassOfAxiom(relaxation,
                activity);
        m.addAxiom(o, poweryogaSub);
        m.addAxiom(o, yogaSubRelax);
        m.addAxiom(o, relaxSubActivity);
        assertEquals(3, o.getAxiomCount());
        AtomicDecomposition ad = new AtomicDecomposerOWLAPITOOLS(o);
        assertEquals(3, ad.getAtoms().size());
        Atom atom = ad.getAtomForAxiom(poweryogaSub);
        assertNotNull(atom);
        System.out
                .println("AtomicDecomposerDepedenciesTest.atomicDecomposerDepedenciesTest() Direct "
                        + ad.getDependencies(atom, true));
        System.out
                .println("AtomicDecomposerDepedenciesTest.atomicDecomposerDepedenciesTest() InDirect "
                        + ad.getDependencies(atom, false));
        assertEquals(3, ad.getDependencies(atom, false).size());
        assertEquals(1, ad.getDependencies(atom, true).size());
        // AtomicDecomposer ad_jfact = new AtomicDecomposerJFactImpl(o, 0);
        // assertEquals(3, ad_jfact.getAtoms().size());
        // Atom atom_jfact = ad.getAtomForAxiom(poweryogaSub);
        // assertNotNull(atom_jfact);
        // assertEquals(3, ad_jfact.getDependencies(atom_jfact, false).size());
        // assertEquals(1, ad_jfact.getDependencies(atom_jfact, true).size());
        DeMoStGUI.run(o, ad);
    }
}
