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
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
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

public class DeMoStView extends AbstractOWLSelectionViewComponent {
	public static final String SELECTED_COLUMN_NAME = "selected";
	private static final String SIZE_COLUMN_NAME = "size";
	private static final String Y_COLUMN_NAME = "y";
	private static final String X_COLUMN_NAME = "x";
	private static final String ACTUAL_Y_COLUMN_NAME = "actual_y";
	private static final String ACTUAL_X_COLUMN_NAME = "actual_x";
	private static final String LABEL_COLUMN_NAME = "label";
	public static final String GENERATING_AXIOM_COLUMN_NAME = "generatingAxiom";
	// Needed because Protege calls updateView() unnecessarily even when the
	// active ontology has not changed.
	private OWLOntology activeOntology;
	private final DefaultListModel selectedEntitiesListModel = new DefaultListModel();
	private final MList selectedEntitiesList = new MList();
	private final JButton showAllGraphButton = new JButton("Show All");
	private Set<OWLObject> wellKnownObjects = null;

	private final class InfluenceTableMode implements TableModel {
		final InfluenceMatrixModel m;

		public InfluenceTableMode(InfluenceMatrixModel m) {
			this.m = m;
		}

		List<TableModelListener> listeners = new ArrayList<TableModelListener>();

		public void addTableModelListener(TableModelListener l) {
			if (listeners.contains(l)) {
				return;
			}
			listeners.add(l);
		}

		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Object.class;
			}
			return Integer.class;
		}

		public int getColumnCount() {
			return 6;
		}

		public String getColumnName(int columnIndex) {
			return influenceMatrixModel.headings[columnIndex];
		}

		public int getRowCount() {
			return influenceMatrixModel.getRecords().size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			final Record record = influenceMatrixModel.getRecord(rowIndex);
			if (columnIndex == 0) {
				return record.entity;
			}
			return record.influenceValues[columnIndex - 1];
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void removeTableModelListener(TableModelListener l) {
			listeners.remove(l);
		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			System.out
					.println("DeMoStView.InfluenceTableMode.setValueAt() NOT DOING IT!");
		}
	}

	private final class RedListener implements ListDataListener,
			ListSelectionListener {
		public void intervalRemoved(ListDataEvent e) {
			refresh(this.selected());
		}

		public void intervalAdded(ListDataEvent e) {
			refresh(this.selected());
		}

		public void contentsChanged(ListDataEvent e) {
			refresh(this.selected());
		}

		/**
		 *
		 */
		protected void refresh(Collection<VisualItem> c) {
			DeMoStView.this.showAllGraphButton
					.setEnabled(DeMoStView.this.selectedEntitiesListModel
							.size() > 0);
			DeMoStView.this.shouldBeVisiblePredicate.reset();
			DeMoStView.this.graphSelectionModel.setSelectedItems(c, this);
			DeMoStView.this.visualisation.run("layout");
		}

		protected List<VisualItem> selected() {
			List<VisualItem> list = new ArrayList<VisualItem>();
			Iterator<?> it = visualisation.items(isSelectedInListPredicate);
			while (it.hasNext()) {
				list.add((VisualItem) it.next());
			}
			return list;
		}

		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				refresh(this.selected());
			}
		}
	}

	private final class OntologyChangeListener implements
			OWLOntologyChangeListener {
		public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
				throws OWLException {
			// TODO: filter out modifications on non logical axioms as they are
			// meaningless for us
			DeMoStView.this.resetDependecies();
			DeMoStView.this.initGraph();
		}
	}

	private final class SelectionListener implements
			GraphSelectionModelListener {
		public void selectionChanged(GraphSelectionEvent graphSelectionEvent) {
			Set<VisualItem> selectedItems = graphSelectionEvent.getSelected();
			Set<Tuple> originalTuples = new HashSet<Tuple>(selectedItems.size());
			for (VisualItem visualItem : selectedItems) {
				originalTuples.add(visualItem.getSourceTuple());
			}
			Iterator<?> tuples = DeMoStView.this.graph.getNodes().tuples();
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
			DeMoStView.this.visualisation.run("color");
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5255020726258623538L;
	private AtomicDecomposition ad;
	private final OWLOntologyChangeListener ontologyChangeListener = new OntologyChangeListener();
	private final Visualization visualisation = new Visualization();
	private final Display display = new Display(this.visualisation);
	private final Graph graph = new Graph(true);
	private OWLObjectList<OWLLogicalAxiom> atomList;
	private GraphSelectionModel graphSelectionModel = new GraphSelectionModel();
	private final SelectionListener selectionListener = new SelectionListener();
	private Predicate isDependantFromSelectionPredicate;
	private ShouldBeVisiblePredicate shouldBeVisiblePredicate;
	private IsSelectedInListPredicate isSelectedInListPredicate;

	@Override
	public void initialiseView() throws Exception {
		ToStringRenderer.getInstance().setRenderer(
				new ManchesterOWLSyntaxOWLObjectRendererImpl());
		this.atomList = new OWLObjectList<OWLLogicalAxiom>(
				this.getOWLEditorKit());
		this.activeOntology = this.getOWLEditorKit().getOWLModelManager()
				.getActiveOntology();
		this.selectedEntitiesList.setModel(this.selectedEntitiesListModel);
		this.selectedEntitiesList.setCellRenderer(new OWLCellRenderer(this
				.getOWLEditorKit()));
		this.setLayout(new BorderLayout());
		this.resetDependecies();
		this.initGraph();
		this.visualisation.add("graph", this.graph);
		this.display.setVisualization(this.visualisation);
		this.getOWLEditorKit().getOWLModelManager().getOWLOntologyManager()
				.addOntologyChangeListener(this.ontologyChangeListener);
		this.initGraphLayout();
		// create a new Display that pull from our Visualization
		this.display.setSize(720, 500); // set display size
		this.display.addControlListener(new DragControl()); // drag items around
		this.display.addControlListener(new PanControl()); // pan with
		// background
		// left-drag
		this.display.addControlListener(new ZoomControl()); // zoom with
		// vertical
		// right-drag
		this.display.addControlListener(new ControlAdapter() {
			@Override
			public void itemClicked(VisualItem item, MouseEvent e) {
				Tuple sourceTuple = item.getSourceTuple();
				if (sourceTuple
						.canGet(GENERATING_AXIOM_COLUMN_NAME, Atom.class)) {
					Atom atom = (Atom) sourceTuple
							.get(GENERATING_AXIOM_COLUMN_NAME);
					if (atom != null) {
						DeMoStView.this.atomList.setListData(atom.getAxioms()
								.toArray());
					}
				}
				DeMoStView.this.graphSelectionModel.setSelectedItems(
						Collections.singleton(item), this);
			}
		});
		JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		topPanel.setDividerLocation(.8);
		topPanel.setResizeWeight(.8);
		JPanel graphPanel = new JPanel(new BorderLayout());
		this.showAllGraphButton.setEnabled(false);
		final RedListener redListener = new RedListener();
		this.selectedEntitiesListModel.addListDataListener(redListener);
		this.selectedEntitiesList.getSelectionModel().addListSelectionListener(
				redListener);
		this.showAllGraphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeMoStView.this.selectedEntitiesListModel.clear();
			}
		});
		this.selectedEntitiesList.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				Object[] selectedValues = DeMoStView.this.selectedEntitiesList
						.getSelectedValues();
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
						&& selectedValues.length > 0) {
					for (Object object : selectedValues) {
						DeMoStView.this.selectedEntitiesListModel
								.removeElement(object);
					}
				}
			}

			public void keyPressed(KeyEvent e) {
			}
		});
		graphPanel.add(this.display, BorderLayout.CENTER);
		graphPanel.add(this.showAllGraphButton, BorderLayout.SOUTH);
		topPanel.setLeftComponent(graphPanel);
		topPanel.setRightComponent(ComponentFactory
				.createScrollPane(this.selectedEntitiesList));
		mainPanel.setTopComponent(topPanel);
		JSplitPane bottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		bottomPanel.setDividerLocation(.8);
		bottomPanel.setResizeWeight(.8);
		bottomPanel.setLeftComponent(ComponentFactory
				.createScrollPane(this.atomList));
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
		final JTable statsTable = new JTable(new InfluenceTableMode(
				influenceMatrixModel));
		statsTable.getTableHeader().addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				influenceMatrixModel.sort(statsTable.getTableHeader()
						.columnAtPoint(e.getPoint()));
			}
		});
		statsTable.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				influenceMatrixModel.sort(statsTable.columnAtPoint(e.getPoint()));
			}
		});
		bottomPanel.setRightComponent(ComponentFactory
				.createScrollPane(statsTable));
		mainPanel.setBottomComponent(bottomPanel);
		mainPanel.setDividerLocation(.7);
		mainPanel.setResizeWeight(.7);
		this.add(mainPanel, BorderLayout.CENTER);
	}

	Map<Atom, Node> allNodes = new HashMap<Atom, Node>();

	private void initGraph() {
		// TODO Listen to the rendering changed event from the model manager and
		// repaint the graph accordingly.
		this.graph.removeSet("graph.nodes");
		this.graph.removeSet("graph.edges");
		this.graph.addColumn(GENERATING_AXIOM_COLUMN_NAME, Atom.class);
		this.graph.addColumn(LABEL_COLUMN_NAME, String.class);
		this.graph.addColumn(SELECTED_COLUMN_NAME, Boolean.class);
		this.graph.addColumn(Y_COLUMN_NAME, Integer.class);
		this.graph.addColumn(X_COLUMN_NAME, Integer.class);
		this.graph.addColumn(ACTUAL_X_COLUMN_NAME, double.class);
		this.graph.addColumn(ACTUAL_Y_COLUMN_NAME, double.class);
		this.graph.addColumn(SIZE_COLUMN_NAME, int.class);
		this.graphSelectionModel
				.removeGraphSelectionModelListener(this.selectionListener);
		this.graphSelectionModel = new GraphSelectionModel();
		this.graphSelectionModel
				.addGraphSelectionModelListener(this.selectionListener);
		Set<Atom> atoms = this.ad.getAtoms();
		allNodes.clear();
		int maxHeight = 0;
		for (Atom atom : atoms) {
			Node node = allNodes.get(atom);
			if (node == null) {
				node = this.graph.addNode();
				node.set(GENERATING_AXIOM_COLUMN_NAME, atom);
				// node.set(LABEL_COLUMN_NAME, Integer.toString(i++));
				node.set(LABEL_COLUMN_NAME, this.computeLabel(atom));
				int height = this.getHeight(atom);
				node.set(Y_COLUMN_NAME, height);
				allNodes.put(atom, node);
				maxHeight = maxHeight < height ? height : maxHeight;
			}
			Set<Atom> dependencies = this.ad.getDependencies(atom, true);
			int size = dependencies.size() - 1;
			node.set(SIZE_COLUMN_NAME, size);
			for (Atom dependantAtom : dependencies) {
				Node depandantNode = allNodes.get(dependantAtom);
				if (depandantNode == null) {
					depandantNode = this.graph.addNode();
					depandantNode.set(GENERATING_AXIOM_COLUMN_NAME,
							dependantAtom);
					depandantNode.set(LABEL_COLUMN_NAME,
							this.computeLabel(dependantAtom));
					int height = this.getHeight(dependantAtom);
					depandantNode.set(Y_COLUMN_NAME, height);
					allNodes.put(dependantAtom, depandantNode);
					maxHeight = maxHeight < height ? height : maxHeight;
				}
				this.graph.addEdge(node, depandantNode);
			}
		}
		List<Node> list = new ArrayList<Node>();
		for (int j = 0; j < this.graph.getNodeCount(); j++) {
			Node node = this.graph.getNode(j);
			list.add(node);
			if (node.canGet(Y_COLUMN_NAME, Integer.class)) {
				Integer newValue = -1
						* ((Integer) node.get(Y_COLUMN_NAME) - maxHeight);
				node.set(Y_COLUMN_NAME, newValue);
			}
		}
		Collections.sort(list, new Comparator<Node>() {
			public int compare(Node node, Node anotherNode) {
				int toReturn = 0;
				if (node == null) {
					toReturn = anotherNode == null ? 0 : -1;
				} else if (anotherNode == null) {
					toReturn = 1;
				} else {
					if (node.canGet(Y_COLUMN_NAME, Integer.class)
							&& anotherNode.canGet(Y_COLUMN_NAME, Integer.class)) {
						Integer aNodeHeight = Integer.parseInt(node.get(
								Y_COLUMN_NAME).toString());
						Integer anotherNodeHeight = Integer
								.parseInt(anotherNode.get(Y_COLUMN_NAME)
										.toString());
						toReturn = (int) Math.signum(aNodeHeight
								- anotherNodeHeight);
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
		this.isDependantFromSelectionPredicate = new IsDependantFromSelectionPredicate(
				this.ad, this.graphSelectionModel);
		this.shouldBeVisiblePredicate = new ShouldBeVisiblePredicate(
				this.selectedEntitiesListModel, this.ad);
		this.isSelectedInListPredicate = new IsSelectedInListPredicate(ad,
				selectedEntitiesListModel, selectedEntitiesList);

	}

	final InfluenceMatrixModel influenceMatrixModel = new InfluenceMatrixModel();

	private int getHeight(Atom atom) {
		if (atom == null) {
			throw new NullPointerException("The axiom cannot be null");
		}
		int toReturn = 1;
		Set<Atom> children = this.ad.getDependencies(atom, true);
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
		this.visualisation.setRendererFactory(new DefaultRendererFactory(r,
				edgeRenderer));
		// DataSizeAction dataSizeAction = new DataSizeAction("graph.nodes",
		// SIZE_COLUMN_NAME);
		FontAction fontAction = new FontAction("graph.nodes",
				OWLRendererPreferences.getInstance().getFont()
						.deriveFont(Font.BOLD));
		ColorAction fill = new ColorAction("graph.nodes", VisualItem.FILLCOLOR,
				ColorLib.rgb(30, 30, 255));
		fill.add(ExpressionParser.predicate("[selected] =true"),
				ColorLib.rgb(255, 0, 0));
		fill.add(this.isDependantFromSelectionPredicate,
				ColorLib.rgb(200, 0, 200));
		// use black for node text
		ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR,
				ColorLib.gray(180));
		// use light grey for edges
		text.add(ExpressionParser.predicate("[selected] =true"),
				ColorLib.gray(0));
		text.add(this.isDependantFromSelectionPredicate, ColorLib.gray(0));
		ColorAction edges = new ColorAction("graph.edges",
				VisualItem.STROKECOLOR, ColorLib.gray(180));
		ColorAction edgesArrows = new ColorAction("graph.edges",
				VisualItem.FILLCOLOR, ColorLib.gray(180));
		VisibilityFilter visibilityFilter = new VisibilityFilter(
				this.shouldBeVisiblePredicate);
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
		this.visualisation.putAction("color", color);
		this.visualisation.putAction("layout", layout);
		this.visualisation.run("color"); // assign the colors
		this.visualisation.run("layout"); // start up the animated layout
	}

	/**
	 * @throws OWLOntologyCreationException
	 */
	protected void resetDependecies() {

		this.ad = new AtomicDecomposerOWLAPITOOLS(this.getOWLEditorKit()
				.getOWLModelManager().getActiveOntology());

		// this.entitiesToAtoms=atomicDecomposition.getEntitiesToAtom();
	}

	@Override
	public void disposeView() {
		this.getOWLEditorKit().getOWLModelManager().getOWLOntologyManager()
				.removeOntologyChangeListener(this.ontologyChangeListener);
	}

	@Override
	protected OWLObject updateView() {
		OWLOntology activeOntology = this.getOWLEditorKit()
				.getOWLModelManager().getActiveOntology();
		if (this.activeOntology != activeOntology) {
			this.activeOntology = activeOntology;
			this.resetDependecies();
			this.initGraph();
		}
		OWLObject selectedObject = this.getOWLEditorKit().getOWLWorkspace()
				.getOWLSelectionModel().getSelectedObject();
		this.updateSelectedList(selectedObject);
		return selectedObject;
	}

	private void updateSelectedList(OWLObject selectedObject) {
		if (selectedObject != null
				&& !this.selectedEntitiesListModel.contains(selectedObject)) {
			this.selectedEntitiesListModel.addElement(selectedObject);
		}
	}

	private String computeLabel(Atom atom) {
		StringBuilder out = new StringBuilder();
		Set<OWLEntity> signatureForLabel = this.computeSignatureForLabel(atom);
		Iterator<OWLEntity> iterator = signatureForLabel.iterator();
		while (iterator.hasNext()) {
			OWLEntity owlEntity = iterator.next();
			String rendering = this.getOWLEditorKit().getOWLModelManager()
					.getRendering(owlEntity);
			out.append(rendering);
			if (iterator.hasNext()) {
				out.append(", ");
			}
		}
		return out.toString();
	}

	private final void buildWellKnownObjects() {
		this.wellKnownObjects = new HashSet<OWLObject>();
		OWLDataFactory dataFactory = this.getOWLEditorKit()
				.getOWLModelManager().getOWLOntologyManager()
				.getOWLDataFactory();
		this.wellKnownObjects.add(dataFactory.getOWLThing());
		this.wellKnownObjects.add(dataFactory.getOWLNothing());
		this.wellKnownObjects.add(dataFactory.getOWLTopDataProperty());
		this.wellKnownObjects.add(dataFactory.getOWLTopObjectProperty());
		OWL2Datatype[] datatypes = OWL2Datatype.values();
		for (OWL2Datatype owl2Datatype : datatypes) {
			this.wellKnownObjects.add(dataFactory.getOWLDatatype(owl2Datatype
					.getIRI()));
		}
	}

	private final Set<OWLObject> getWellKnownObjects() {
		if (this.wellKnownObjects == null) {
			this.buildWellKnownObjects();
		}
		return this.wellKnownObjects;
	}

	// private MultiMap<OWLEntity, Atom> entitiesToAtoms=new MultiMap<OWLEntity,
	// Atom>();

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
