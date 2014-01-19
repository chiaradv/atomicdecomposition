package uk.ac.manchester.cs.demost.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.protege.editor.core.ui.list.MList;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.ui.list.OWLObjectList;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.protege.editor.owl.ui.renderer.OWLRendererPreferences;
import org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.VisibilityFilter;
import prefuse.action.layout.SpecifiedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposerOWLAPITOOLS;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

/** The Class DeMoStView. */
public class DeMoStView extends AbstractOWLSelectionViewComponent {
    /** The Constant SELECTED_COLUMN_NAME. */
    public static final String SELECTED_COLUMN_NAME = "selected";
    /** The Constant SIZE_COLUMN_NAME. */
    private static final String SIZE_COLUMN_NAME = "size";
    /** The Constant Y_COLUMN_NAME. */
    private static final String Y_COLUMN_NAME = "y";
    /** The Constant X_COLUMN_NAME. */
    private static final String X_COLUMN_NAME = "x";
    /** The Constant ACTUAL_Y_COLUMN_NAME. */
    private static final String ACTUAL_Y_COLUMN_NAME = "actual_y";
    /** The Constant ACTUAL_X_COLUMN_NAME. */
    private static final String ACTUAL_X_COLUMN_NAME = "actual_x";
    /** The Constant LABEL_COLUMN_NAME. */
    private static final String LABEL_COLUMN_NAME = "label";
    /** The Constant GENERATING_AXIOM_COLUMN_NAME. */
    public static final String GENERATING_AXIOM_COLUMN_NAME = "generatingAxiom";
    // Needed because Protege calls updateView() unnecessarily even when the
    // active ontology has not changed.
    /** The active ontology. */
    private OWLOntology activeOntology;
    /** The selected entities list model. */
    private final DefaultListModel selectedEntitiesListModel = new DefaultListModel();
    /** The selected entities list. */
    private final MList selectedEntitiesList = new MList();
    /** The show all graph button. */
    private final JButton showAllGraphButton = new JButton("Show All");
    /** The well known objects. */
    private Set<OWLObject> wellKnownObjects = null;

    /** The Class InfluenceTableMode. */
    private final class InfluenceTableMode implements TableModel {
        /** The m. */
        final InfluenceMatrixModel m;

        /** Instantiates a new influence table mode.
         * 
         * @param m
         *            the m */
        public InfluenceTableMode(InfluenceMatrixModel m) {
            this.m = m;
        }

        /** The listeners. */
        List<TableModelListener> listeners = new ArrayList<TableModelListener>();

        @Override
        public void addTableModelListener(TableModelListener l) {
            if (listeners.contains(l)) {
                return;
            }
            listeners.add(l);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Object.class;
            }
            return Integer.class;
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return influenceMatrixModel.headings[columnIndex];
        }

        @Override
        public int getRowCount() {
            return influenceMatrixModel.getRecords().size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final Record record = influenceMatrixModel.getRecord(rowIndex);
            if (columnIndex == 0) {
                return record.entity;
            }
            return record.influenceValues[columnIndex - 1];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            System.out
                    .println("DeMoStView.InfluenceTableMode.setValueAt() NOT DOING IT!");
        }
    }

    /** The listener interface for receiving red events. The class that is
     * interested in processing a red event implements this interface, and the
     * object created with that class is registered with a component using the
     * component's <code>addRedListener</code> method. When the red event
     * occurs, that object's appropriate method is invoked. */
    private final class RedListener implements ListDataListener, ListSelectionListener {
        @Override
        public void intervalRemoved(ListDataEvent e) {
            refresh(selected());
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            refresh(selected());
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            refresh(selected());
        }

        /** Refresh.
         * 
         * @param c
         *            the c */
        protected void refresh(Collection<VisualItem> c) {
            showAllGraphButton.setEnabled(selectedEntitiesListModel.size() > 0);
            shouldBeVisiblePredicate.reset();
            graphSelectionModel.setSelectedItems(c, this);
            visualisation.run("layout");
        }

        /** Selected.
         * 
         * @return the list */
        protected List<VisualItem> selected() {
            List<VisualItem> list = new ArrayList<VisualItem>();
            Iterator<?> it = visualisation.items(isSelectedInListPredicate);
            while (it.hasNext()) {
                list.add((VisualItem) it.next());
            }
            return list;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                refresh(selected());
            }
        }
    }

    /** The listener interface for receiving ontologyChange events. The class
     * that is interested in processing a ontologyChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's <code>addOntologyChangeListener</code>
     * method. When the ontologyChange event occurs, that object's appropriate
     * method is invoked. */
    private final class OntologyChangeListener implements OWLOntologyChangeListener {
        @Override
        public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
                throws OWLException {
            // TODO: filter out modifications on non logical axioms as they are
            // meaningless for us
            resetDependecies();
            initGraph();
        }
    }

    /** The listener interface for receiving selection events. The class that is
     * interested in processing a selection event implements this interface, and
     * the object created with that class is registered with a component using
     * the component's <code>addSelectionListener</code> method. When the
     * selection event occurs, that object's appropriate method is invoked. */
    private final class SelectionListener implements GraphSelectionModelListener {
        @Override
        public void selectionChanged(GraphSelectionEvent graphSelectionEvent) {
            Set<VisualItem> selectedItems = graphSelectionEvent.getSelected();
            Set<Tuple> originalTuples = new HashSet<Tuple>(selectedItems.size());
            for (VisualItem visualItem : selectedItems) {
                originalTuples.add(visualItem.getSourceTuple());
            }
            Iterator<?> tuples = graph.getNodes().tuples();
            while (tuples.hasNext()) {
                Tuple t = (Tuple) tuples.next();
                if (t.canSet(SELECTED_COLUMN_NAME, Boolean.class)) {
                    t.set(SELECTED_COLUMN_NAME, Boolean.FALSE);
                }
            }
            for (Tuple tuple : originalTuples) {
                if (tuple.canSet(SELECTED_COLUMN_NAME, Boolean.class)) {
                    tuple.set(SELECTED_COLUMN_NAME, Boolean.TRUE);
                }
            }
            visualisation.run("color");
        }
    }

    private static final long serialVersionUID = -5255020726258623538L;
    /** The ad. */
    private AtomicDecomposition ad;
    /** The ontology change listener. */
    private final OWLOntologyChangeListener ontologyChangeListener = new OntologyChangeListener();
    /** The visualisation. */
    private final Visualization visualisation = new Visualization();
    /** The display. */
    private final Display display = new Display(visualisation);
    /** The graph. */
    private final Graph graph = new Graph(true);
    /** The atom list. */
    private OWLObjectList<OWLLogicalAxiom> atomList;
    /** The graph selection model. */
    private GraphSelectionModel graphSelectionModel = new GraphSelectionModel();
    /** The selection listener. */
    private final SelectionListener selectionListener = new SelectionListener();
    /** The is dependant from selection predicate. */
    private Predicate isDependantFromSelectionPredicate;
    /** The should be visible predicate. */
    private ShouldBeVisiblePredicate shouldBeVisiblePredicate;
    /** The is selected in list predicate. */
    private IsSelectedInListPredicate isSelectedInListPredicate;

    @Override
    public void initialiseView() throws Exception {
        ToStringRenderer.getInstance().setRenderer(
                new ManchesterOWLSyntaxOWLObjectRendererImpl());
        atomList = new OWLObjectList<OWLLogicalAxiom>(getOWLEditorKit());
        activeOntology = getOWLEditorKit().getOWLModelManager().getActiveOntology();
        selectedEntitiesList.setModel(selectedEntitiesListModel);
        selectedEntitiesList.setCellRenderer(new OWLCellRenderer(getOWLEditorKit()));
        setLayout(new BorderLayout());
        resetDependecies();
        initGraph();
        visualisation.add("graph", graph);
        display.setVisualization(visualisation);
        getOWLEditorKit().getOWLModelManager().getOWLOntologyManager()
                .addOntologyChangeListener(ontologyChangeListener);
        initGraphLayout();
        // create a new Display that pull from our Visualization
        display.setSize(720, 500); // set display size
        display.addControlListener(new DragControl()); // drag items around
        display.addControlListener(new PanControl()); // pan with
        // background
        // left-drag
        display.addControlListener(new ZoomControl()); // zoom with
        // vertical
        // right-drag
        display.addControlListener(new ControlAdapter() {
            @Override
            public void itemClicked(VisualItem item, MouseEvent e) {
                Tuple sourceTuple = item.getSourceTuple();
                if (sourceTuple.canGet(GENERATING_AXIOM_COLUMN_NAME, Atom.class)) {
                    Atom atom = (Atom) sourceTuple.get(GENERATING_AXIOM_COLUMN_NAME);
                    if (atom != null) {
                        atomList.setListData(atom.getAxioms().toArray());
                    }
                }
                graphSelectionModel.setSelectedItems(Collections.singleton(item), this);
            }
        });
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topPanel.setDividerLocation(.8);
        topPanel.setResizeWeight(.8);
        JPanel graphPanel = new JPanel(new BorderLayout());
        showAllGraphButton.setEnabled(false);
        final RedListener redListener = new RedListener();
        selectedEntitiesListModel.addListDataListener(redListener);
        selectedEntitiesList.getSelectionModel().addListSelectionListener(redListener);
        showAllGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedEntitiesListModel.clear();
            }
        });
        selectedEntitiesList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                Object[] selectedValues = selectedEntitiesList.getSelectedValues();
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && selectedValues.length > 0) {
                    for (Object object : selectedValues) {
                        selectedEntitiesListModel.removeElement(object);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}
        });
        graphPanel.add(display, BorderLayout.CENTER);
        graphPanel.add(showAllGraphButton, BorderLayout.SOUTH);
        topPanel.setLeftComponent(graphPanel);
        topPanel.setRightComponent(ComponentFactory
                .createScrollPane(selectedEntitiesList));
        mainPanel.setTopComponent(topPanel);
        JSplitPane bottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomPanel.setDividerLocation(.8);
        bottomPanel.setResizeWeight(.8);
        bottomPanel.setLeftComponent(ComponentFactory.createScrollPane(atomList));
        for (OWLEntity key : ad.getTermBasedIndex().keySet()) {
            Record record = new Record();
            record.setEntity(key);
            int[] values = new int[5];
            Collection<Atom> value = ad.getTermBasedIndex().get(key);
            values[0] = value.size();
            Set<Atom> setAtoms = new HashSet<Atom>();
            for (Atom a : value) {
                setAtoms.addAll(ad.getDependencies(a));
            }
            setAtoms.removeAll(value);
            values[1] = setAtoms.size();
            setAtoms.clear();
            for (Atom a : value) {
                setAtoms.addAll(ad.getDependents(a));
            }
            setAtoms.removeAll(value);
            values[2] = setAtoms.size();
            setAtoms.clear();
            for (Atom a : value) {
                setAtoms.addAll(ad.getDependencies(a, true));
            }
            setAtoms.removeAll(value);
            values[3] = setAtoms.size();
            setAtoms.clear();
            for (Atom a : value) {
                setAtoms.addAll(ad.getDependents(a, true));
            }
            setAtoms.removeAll(value);
            values[4] = setAtoms.size();
            record.setValues(values);
            influenceMatrixModel.addRecord(record);
        }
        influenceMatrixModel.sort(1);
        // try {
        // PrintStream p=new
        // PrintStream("/Users/macbook/Desktop/influenceData.csv");
        // p.println(influenceMatrixModel);
        // p.close();
        // }catch(Exception e) {
        // e.printStackTrace();
        // }
        // System.out.println("DeMoStView.initialiseView() " +
        // influenceMatrixModel);
        final JTable statsTable = new JTable(new InfluenceTableMode(influenceMatrixModel));
        statsTable.getTableHeader().addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {
                influenceMatrixModel.sort(statsTable.getTableHeader().columnAtPoint(
                        e.getPoint()));
            }
        });
        statsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {
                influenceMatrixModel.sort(statsTable.columnAtPoint(e.getPoint()));
            }
        });
        bottomPanel.setRightComponent(ComponentFactory.createScrollPane(statsTable));
        mainPanel.setBottomComponent(bottomPanel);
        mainPanel.setDividerLocation(.7);
        mainPanel.setResizeWeight(.7);
        this.add(mainPanel, BorderLayout.CENTER);
    }

    /** The all nodes. */
    Map<Atom, Node> allNodes = new HashMap<Atom, Node>();

    /** Inits the graph. */
    private void initGraph() {
        // TODO Listen to the rendering changed event from the model manager and
        // repaint the graph accordingly.
        graph.removeSet("graph.nodes");
        graph.removeSet("graph.edges");
        graph.addColumn(GENERATING_AXIOM_COLUMN_NAME, Atom.class);
        graph.addColumn(LABEL_COLUMN_NAME, String.class);
        graph.addColumn(SELECTED_COLUMN_NAME, Boolean.class);
        graph.addColumn(Y_COLUMN_NAME, Integer.class);
        graph.addColumn(X_COLUMN_NAME, Integer.class);
        graph.addColumn(ACTUAL_X_COLUMN_NAME, double.class);
        graph.addColumn(ACTUAL_Y_COLUMN_NAME, double.class);
        graph.addColumn(SIZE_COLUMN_NAME, int.class);
        graphSelectionModel.removeGraphSelectionModelListener(selectionListener);
        graphSelectionModel = new GraphSelectionModel();
        graphSelectionModel.addGraphSelectionModelListener(selectionListener);
        Set<Atom> atoms = ad.getAtoms();
        allNodes.clear();
        int maxHeight = 0;
        for (Atom atom : atoms) {
            Node node = allNodes.get(atom);
            if (node == null) {
                node = graph.addNode();
                node.set(GENERATING_AXIOM_COLUMN_NAME, atom);
                // node.set(LABEL_COLUMN_NAME, Integer.toString(i++));
                node.set(LABEL_COLUMN_NAME, computeLabel(atom));
                int height = this.getHeight(atom);
                node.set(Y_COLUMN_NAME, height);
                allNodes.put(atom, node);
                maxHeight = maxHeight < height ? height : maxHeight;
            }
            Set<Atom> dependencies = ad.getDependencies(atom, true);
            int size = dependencies.size() - 1;
            node.set(SIZE_COLUMN_NAME, size);
            for (Atom dependantAtom : dependencies) {
                Node depandantNode = allNodes.get(dependantAtom);
                if (depandantNode == null) {
                    depandantNode = graph.addNode();
                    depandantNode.set(GENERATING_AXIOM_COLUMN_NAME, dependantAtom);
                    depandantNode.set(LABEL_COLUMN_NAME, computeLabel(dependantAtom));
                    int height = this.getHeight(dependantAtom);
                    depandantNode.set(Y_COLUMN_NAME, height);
                    allNodes.put(dependantAtom, depandantNode);
                    maxHeight = maxHeight < height ? height : maxHeight;
                }
                graph.addEdge(node, depandantNode);
            }
        }
        List<Node> list = new ArrayList<Node>();
        for (int j = 0; j < graph.getNodeCount(); j++) {
            Node node = graph.getNode(j);
            list.add(node);
            if (node.canGet(Y_COLUMN_NAME, Integer.class)) {
                Integer newValue = -1 * ((Integer) node.get(Y_COLUMN_NAME) - maxHeight);
                node.set(Y_COLUMN_NAME, newValue);
            }
        }
        Collections.sort(list, new Comparator<Node>() {
            @Override
            public int compare(Node node, Node anotherNode) {
                int toReturn = 0;
                if (node == null) {
                    toReturn = anotherNode == null ? 0 : -1;
                } else if (anotherNode == null) {
                    toReturn = 1;
                } else {
                    if (node.canGet(Y_COLUMN_NAME, Integer.class)
                            && anotherNode.canGet(Y_COLUMN_NAME, Integer.class)) {
                        Integer aNodeHeight = Integer.parseInt(node.get(Y_COLUMN_NAME)
                                .toString());
                        Integer anotherNodeHeight = Integer.parseInt(anotherNode.get(
                                Y_COLUMN_NAME).toString());
                        toReturn = (int) Math.signum(aNodeHeight - anotherNodeHeight);
                    } else {
                        toReturn = (int) Math.signum(node.hashCode()
                                - anotherNode.hashCode());
                    }
                }
                return toReturn;
            }
        });
        int startX = 1;
        if (!list.isEmpty()) {
            int currentTier = (Integer) list.get(0).get(Y_COLUMN_NAME);
            for (Node node : list) {
                int nodeHeight = (Integer) node.get(Y_COLUMN_NAME);
                if (nodeHeight != currentTier) {
                    startX = 1;
                    currentTier = nodeHeight;
                }
                node.set(X_COLUMN_NAME, startX);
                startX++;
            }
            double pace = 200;
            for (Node node : list) {
                if (node.canGet(Y_COLUMN_NAME, Integer.class)
                        && node.canSet(ACTUAL_Y_COLUMN_NAME, Double.class)) {
                    double height = (Integer) node.get(Y_COLUMN_NAME);
                    node.set(ACTUAL_Y_COLUMN_NAME, pace * height);
                }
                if (node.canGet(X_COLUMN_NAME, Integer.class)
                        && node.canSet(ACTUAL_X_COLUMN_NAME, Double.class)) {
                    double width = (Integer) node.get(X_COLUMN_NAME);
                    node.set(ACTUAL_X_COLUMN_NAME, pace * width);
                }
            }
        }
        isDependantFromSelectionPredicate = new IsDependantFromSelectionPredicate(ad,
                graphSelectionModel);
        shouldBeVisiblePredicate = new ShouldBeVisiblePredicate(
                selectedEntitiesListModel, ad);
        isSelectedInListPredicate = new IsSelectedInListPredicate(ad,
                selectedEntitiesListModel, selectedEntitiesList);
    }

    /** The influence matrix model. */
    final InfluenceMatrixModel influenceMatrixModel = new InfluenceMatrixModel();

    /** Gets the height.
     * 
     * @param atom
     *            the atom
     * @return the height */
    private int getHeight(Atom atom) {
        if (atom == null) {
            throw new NullPointerException("The axiom cannot be null");
        }
        int toReturn = 1;
        Set<Atom> children = ad.getDependencies(atom, true);
        int max = 0;
        if (children != null) {
            for (Atom child : children) {
                if (!child.equals(atom)) {
                    int childHeight = this.getHeight(child);
                    max = max < childHeight ? childHeight : max;
                }
            }
        }
        return toReturn + max;
    }

    /** Inits the graph layout. */
    private void initGraphLayout() {
        // draw the "name" label for NodeItems
        LabelRenderer r = new LabelRenderer();
        r.setRoundedCorner(8, 8);// round the corners
        r.setVerticalPadding(10);
        r.setHorizontalPadding(10);
        // create a new default renderer factory
        // return our name label renderer as the default for all non-EdgeItems
        // includes straight line edges for EdgeItems by default
        EdgeRenderer edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_LINE,
                Constants.EDGE_ARROW_FORWARD);
        edgeRenderer.setArrowHeadSize(10, 10);
        edgeRenderer.setArrowType(Constants.EDGE_ARROW_FORWARD);
        visualisation.setRendererFactory(new DefaultRendererFactory(r, edgeRenderer));
        // DataSizeAction dataSizeAction = new DataSizeAction("graph.nodes",
        // SIZE_COLUMN_NAME);
        FontAction fontAction = new FontAction("graph.nodes", OWLRendererPreferences
                .getInstance().getFont().deriveFont(Font.BOLD));
        ColorAction fill = new ColorAction("graph.nodes", VisualItem.FILLCOLOR,
                ColorLib.rgb(30, 30, 255));
        fill.add(ExpressionParser.predicate("[selected] =true"), ColorLib.rgb(255, 0, 0));
        fill.add(isDependantFromSelectionPredicate, ColorLib.rgb(200, 0, 200));
        // use black for node text
        ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR,
                ColorLib.gray(180));
        // use light grey for edges
        text.add(ExpressionParser.predicate("[selected] =true"), ColorLib.gray(0));
        text.add(isDependantFromSelectionPredicate, ColorLib.gray(0));
        ColorAction edges = new ColorAction("graph.edges", VisualItem.STROKECOLOR,
                ColorLib.gray(180));
        ColorAction edgesArrows = new ColorAction("graph.edges", VisualItem.FILLCOLOR,
                ColorLib.gray(180));
        VisibilityFilter visibilityFilter = new VisibilityFilter(shouldBeVisiblePredicate);
        // create an action list containing all color assignments
        ActionList color = new ActionList();
        color.add(new RepaintAction());
        color.add(fontAction);
        color.add(fill);
        color.add(text);
        color.add(edges);
        color.add(edgesArrows);
        // color.add(dataSizeAction);
        // create an action list with an animated layout
        // the INFINITY parameter tells the action list to run indefinitely
        ActionList layout = new ActionList(Activity.DEFAULT_STEP_TIME);
        layout.add(new SpecifiedLayout("graph.nodes", ACTUAL_X_COLUMN_NAME,
                ACTUAL_Y_COLUMN_NAME));
        layout.add(visibilityFilter);
        layout.add(new RepaintAction());
        // add the actions to the visualization
        visualisation.putAction("color", color);
        visualisation.putAction("layout", layout);
        visualisation.run("color"); // assign the colors
        visualisation.run("layout"); // start up the animated layout
    }

    /** Reset dependecies. */
    protected void resetDependecies() {
        ad = new AtomicDecomposerOWLAPITOOLS(getOWLEditorKit().getOWLModelManager()
                .getActiveOntology());
        // this.entitiesToAtoms=atomicDecomposition.getEntitiesToAtom();
    }

    @Override
    public void disposeView() {
        getOWLEditorKit().getOWLModelManager().getOWLOntologyManager()
                .removeOntologyChangeListener(ontologyChangeListener);
    }

    @Override
    protected OWLObject updateView() {
        OWLOntology activeOntology = getOWLEditorKit().getOWLModelManager()
                .getActiveOntology();
        if (this.activeOntology != activeOntology) {
            this.activeOntology = activeOntology;
            resetDependecies();
            initGraph();
        }
        OWLObject selectedObject = getOWLEditorKit().getOWLWorkspace()
                .getOWLSelectionModel().getSelectedObject();
        updateSelectedList(selectedObject);
        return selectedObject;
    }

    /** Update selected list.
     * 
     * @param selectedObject
     *            the selected object */
    private void updateSelectedList(OWLObject selectedObject) {
        if (selectedObject != null && !selectedEntitiesListModel.contains(selectedObject)) {
            selectedEntitiesListModel.addElement(selectedObject);
        }
    }

    /** Compute label.
     * 
     * @param atom
     *            the atom
     * @return the string */
    private String computeLabel(Atom atom) {
        StringBuilder out = new StringBuilder();
        Set<OWLEntity> signatureForLabel = computeSignatureForLabel(atom);
        Iterator<OWLEntity> iterator = signatureForLabel.iterator();
        while (iterator.hasNext()) {
            OWLEntity owlEntity = iterator.next();
            String rendering = getOWLEditorKit().getOWLModelManager().getRendering(
                    owlEntity);
            out.append(rendering);
            if (iterator.hasNext()) {
                out.append(", ");
            }
        }
        return out.toString();
    }

    /** Builds the well known objects. */
    private final void buildWellKnownObjects() {
        wellKnownObjects = new HashSet<OWLObject>();
        OWLDataFactory dataFactory = getOWLEditorKit().getOWLModelManager()
                .getOWLOntologyManager().getOWLDataFactory();
        wellKnownObjects.add(dataFactory.getOWLThing());
        wellKnownObjects.add(dataFactory.getOWLNothing());
        wellKnownObjects.add(dataFactory.getOWLTopDataProperty());
        wellKnownObjects.add(dataFactory.getOWLTopObjectProperty());
        OWL2Datatype[] datatypes = OWL2Datatype.values();
        for (OWL2Datatype owl2Datatype : datatypes) {
            wellKnownObjects.add(dataFactory.getOWLDatatype(owl2Datatype.getIRI()));
        }
    }

    /** Gets the well known objects.
     * 
     * @return the well known objects */
    private final Set<OWLObject> getWellKnownObjects() {
        if (wellKnownObjects == null) {
            buildWellKnownObjects();
        }
        return wellKnownObjects;
    }

    // private MultiMap<OWLEntity, Atom> entitiesToAtoms=new MultiMap<OWLEntity,
    // Atom>();
    /** Compute signature for label.
     * 
     * @param atom
     *            the atom
     * @return the sets the */
    private Set<OWLEntity> computeSignatureForLabel(Atom atom) {
        Set<OWLEntity> signature = new HashSet<OWLEntity>();
        for (OWLEntity e : ad.getTermBasedIndex().keySet()) {
            // XXX: ad.getTermBasedIndex() is not a multi map
            Map<OWLEntity, Set<Atom>> termBasedIndex = ad.getTermBasedIndex();
            if (termBasedIndex.containsKey(e)) {
                if (termBasedIndex.get(e).equals(atom)) {
                    signature.add(e);
                }
            }
            // if(atomicDecomposition.getEntitiesToAtom().contains(e, atom)) {
            // signature.add(e);
            // }
        }
        return signature;
    }
}
