/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefuse.txt" for licensing terms.
 */
package prefuse.data;

import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;

import prefuse.data.tuple.TupleManager;
import prefuse.visual.tuple.TableEdgeItem;

/** Special tree instance for storing a spanning tree over a graph instance. The
 * spanning tree ensures that only Node and Edge instances from the backing
 * Graph are returned, so requesting nodes, edges, or iterators over this
 * spanning tree will return the desired Node or Edge tuples from the backing
 * graph this tree spans.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class SpanningTree extends Tree {
    /** Extra edge table data field recording the id of the source edge a tree
     * edge represents. */
    public static final String SOURCE_EDGE = "source";
    /** Edge table schema used by the spanning tree. */
    protected static final Schema EDGE_SCHEMA = new Schema();
    static {
        EDGE_SCHEMA.addColumn(DEFAULT_SOURCE_KEY, int.class, new Integer(-1));
        EDGE_SCHEMA.addColumn(DEFAULT_TARGET_KEY, int.class, new Integer(-1));
        EDGE_SCHEMA.addColumn(SOURCE_EDGE, int.class);
    }
    /** A reference to the backing graph that this tree spans. */
    protected Graph m_backing;

    /** Create a new SpanningTree.
     * 
     * @param g
     *            the backing Graph to span
     * @param root
     *            the Node to use as the root of the spanning tree */
    public SpanningTree(Graph g, Node root) {
        super(g.getNodeTable(), EDGE_SCHEMA.instantiate());
        m_backing = g;
        TupleManager etm = new TupleManager(getEdgeTable(), null, TableEdgeItem.class) {
            @Override
            public Tuple getTuple(int row) {
                return m_backing.getEdge(m_table.getInt(row, SOURCE_EDGE));
            }
        };
        getEdgeTable().setTupleManager(etm);
        super.setTupleManagers(g.m_nodeTuples, etm);
        buildSpanningTree(root);
    }

    /** Build the spanning tree, starting at the given root. Uses an unweighted
     * breadth first traversal to build the spanning tree.
     * 
     * @param root
     *            the root node of the spanning tree */
    public void buildSpanningTree(Node root) {
        // re-use a previously allocated tree if possible
        super.clearEdges();
        super.setRoot(root);
        // build unweighted spanning tree by BFS
        LinkedList q = new LinkedList();
        BitSet visit = new BitSet();
        q.add(root);
        visit.set(root.getRow());
        Table edges = getEdgeTable();
        while (!q.isEmpty()) {
            Node p = (Node) q.removeFirst();
            for (Iterator iter = p.edges(); iter.hasNext();) {
                Edge e = (Edge) iter.next();
                Node n = e.getAdjacentNode(p);
                if (!visit.get(n.getRow())) {
                    q.add(n);
                    visit.set(n.getRow());
                    int er = super.addChildEdge(p.getRow(), n.getRow());
                    edges.setInt(er, SOURCE_EDGE, e.getRow());
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Disallow most mutator methods
    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param parent
     *            the parent
     * @return the int
     * @see prefuse.data.Tree#addChild(int) */
    @Override
    public int addChild(int parent) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param parent
     *            the parent
     * @return the node
     * @see prefuse.data.Tree#addChild(prefuse.data.Node) */
    @Override
    public Node addChild(Node parent) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param parent
     *            the parent
     * @param child
     *            the child
     * @return the int
     * @see prefuse.data.Tree#addChildEdge(int, int) */
    @Override
    public int addChildEdge(int parent, int child) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param parent
     *            the parent
     * @param child
     *            the child
     * @return the edge
     * @see prefuse.data.Tree#addChildEdge(prefuse.data.Node, prefuse.data.Node) */
    @Override
    public Edge addChildEdge(Node parent, Node child) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @return the node
     * @see prefuse.data.Tree#addRoot() */
    @Override
    public Node addRoot() {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @return the int
     * @see prefuse.data.Tree#addRootRow() */
    @Override
    public int addRootRow() {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param node
     *            the node
     * @return true, if successful
     * @see prefuse.data.Tree#removeChild(int) */
    @Override
    public boolean removeChild(int node) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param n
     *            the n
     * @return true, if successful
     * @see prefuse.data.Tree#removeChild(prefuse.data.Node) */
    @Override
    public boolean removeChild(Node n) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param e
     *            the e
     * @return true, if successful
     * @see prefuse.data.Tree#removeChildEdge(prefuse.data.Edge) */
    @Override
    public boolean removeChildEdge(Edge e) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param edge
     *            the edge
     * @return true, if successful
     * @see prefuse.data.Tree#removeChildEdge(int) */
    @Override
    public boolean removeChildEdge(int edge) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param root
     *            the new root
     * @see prefuse.data.Tree#setRoot(prefuse.data.Node) */
    @Override
    void setRoot(Node root) {
        throw new UnsupportedOperationException(
                "Changes to tree structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param s
     *            the s
     * @param t
     *            the t
     * @return the int
     * @see prefuse.data.Graph#addEdge(int, int) */
    @Override
    public int addEdge(int s, int t) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param s
     *            the s
     * @param t
     *            the t
     * @return the edge
     * @see prefuse.data.Graph#addEdge(prefuse.data.Node, prefuse.data.Node) */
    @Override
    public Edge addEdge(Node s, Node t) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @return the node
     * @see prefuse.data.Graph#addNode() */
    @Override
    public Node addNode() {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @return the int
     * @see prefuse.data.Graph#addNodeRow() */
    @Override
    public int addNodeRow() {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @see prefuse.data.tuple.TupleSet#clear() */
    @Override
    public void clear() {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param e
     *            the e
     * @return true, if successful
     * @see prefuse.data.Graph#removeEdge(prefuse.data.Edge) */
    @Override
    public boolean removeEdge(Edge e) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param edge
     *            the edge
     * @return true, if successful
     * @see prefuse.data.Graph#removeEdge(int) */
    @Override
    public boolean removeEdge(int edge) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param node
     *            the node
     * @return true, if successful
     * @see prefuse.data.Graph#removeNode(int) */
    @Override
    public boolean removeNode(int node) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param n
     *            the n
     * @return true, if successful
     * @see prefuse.data.Graph#removeNode(prefuse.data.Node) */
    @Override
    public boolean removeNode(Node n) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param t
     *            the t
     * @return true, if successful
     * @see prefuse.data.tuple.TupleSet#removeTuple(prefuse.data.Tuple) */
    @Override
    public boolean removeTuple(Tuple t) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param edges
     *            the new edge table
     * @see prefuse.data.Graph#setEdgeTable(prefuse.data.Table) */
    @Override
    public void setEdgeTable(Table edges) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }

    /** Unsupported operation. Spanning trees should not be edited.
     * 
     * @param ntm
     *            the ntm
     * @param etm
     *            the etm
     * @see prefuse.data.Graph#setTupleManagers(prefuse.data.tuple.TupleManager,
     *      prefuse.data.tuple.TupleManager) */
    @Override
    public void setTupleManagers(TupleManager ntm, TupleManager etm) {
        throw new UnsupportedOperationException(
                "Changes to graph structure not allowed for spanning trees.");
    }
} // end of class SpanningTree
