package prefuse.visual.tuple;

import java.util.Iterator;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.visual.NodeItem;

/** NodeItem implementation that used data values from a backing VisualTable of
 * nodes.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class TableNodeItem extends TableVisualItem implements NodeItem {
    /** The m_graph. */
    protected Graph m_graph;

    /** Initialize a new TableNodeItem for the given graph, table, and row. This
     * method is used by the appropriate TupleManager instance, and should not
     * be called directly by client code, unless by a client-supplied custom
     * TupleManager.
     * 
     * @param table
     *            the backing VisualTable
     * @param graph
     *            the backing VisualGraph
     * @param row
     *            the row in the node table to which this Node instance
     *            corresponds. */
    @Override
    protected void init(Table table, Graph graph, int row) {
        m_table = table;
        m_graph = graph;
        m_row = m_table.isValidRow(row) ? row : -1;
    }

    /** Gets the graph.
     * 
     * @return the graph
     * @see prefuse.data.Node#getGraph() */
    @Override
    public Graph getGraph() {
        return m_graph;
    }

    // ------------------------------------------------------------------------
    // If only we had multiple inheritance or categories....
    // Instead we must re-implement the entire Node interface.
    /** Gets the in degree.
     * 
     * @return the in degree
     * @see prefuse.data.Node#getInDegree() */
    @Override
    public int getInDegree() {
        return m_graph.getInDegree(this);
    }

    /** Gets the out degree.
     * 
     * @return the out degree
     * @see prefuse.data.Node#getOutDegree() */
    @Override
    public int getOutDegree() {
        return m_graph.getOutDegree(this);
    }

    /** Gets the degree.
     * 
     * @return the degree
     * @see prefuse.data.Node#getDegree() */
    @Override
    public int getDegree() {
        return m_graph.getDegree(this);
    }

    /** In edges.
     * 
     * @return the iterator
     * @see prefuse.data.Node#inEdges() */
    @Override
    public Iterator inEdges() {
        return m_graph.inEdges(this);
    }

    /** Out edges.
     * 
     * @return the iterator
     * @see prefuse.data.Node#outEdges() */
    @Override
    public Iterator outEdges() {
        return m_graph.outEdges(this);
    }

    /** Edges.
     * 
     * @return the iterator
     * @see prefuse.data.Node#edges() */
    @Override
    public Iterator edges() {
        return m_graph.edges(this);
    }

    /** In neighbors.
     * 
     * @return the iterator
     * @see prefuse.data.Node#inNeighbors() */
    @Override
    public Iterator inNeighbors() {
        return m_graph.inNeighbors(this);
    }

    /** Out neighbors.
     * 
     * @return the iterator
     * @see prefuse.data.Node#outNeighbors() */
    @Override
    public Iterator outNeighbors() {
        return m_graph.outNeighbors(this);
    }

    /** Neighbors.
     * 
     * @return the iterator
     * @see prefuse.data.Node#neighbors() */
    @Override
    public Iterator neighbors() {
        return m_graph.neighbors(this);
    }

    // ------------------------------------------------------------------------
    /** Gets the parent.
     * 
     * @return the parent
     * @see prefuse.data.Node#getParent() */
    @Override
    public Node getParent() {
        return m_graph.getSpanningTree().getParent(this);
    }

    /** Gets the parent edge.
     * 
     * @return the parent edge
     * @see prefuse.data.Node#getParentEdge() */
    @Override
    public Edge getParentEdge() {
        return m_graph.getSpanningTree().getParentEdge(this);
    }

    /** Gets the child count.
     * 
     * @return the child count
     * @see prefuse.data.Node#getChildCount() */
    @Override
    public int getChildCount() {
        return m_graph.getSpanningTree().getChildCount(m_row);
    }

    /** Gets the child index.
     * 
     * @param child
     *            the child
     * @return the child index
     * @see prefuse.data.Node#getChildIndex(prefuse.data.Node) */
    @Override
    public int getChildIndex(Node child) {
        return m_graph.getSpanningTree().getChildIndex(this, child);
    }

    /** Gets the child.
     * 
     * @param idx
     *            the idx
     * @return the child
     * @see prefuse.data.Node#getChild(int) */
    @Override
    public Node getChild(int idx) {
        return m_graph.getSpanningTree().getChild(this, idx);
    }

    /** Gets the first child.
     * 
     * @return the first child
     * @see prefuse.data.Node#getFirstChild() */
    @Override
    public Node getFirstChild() {
        return m_graph.getSpanningTree().getFirstChild(this);
    }

    /** Gets the last child.
     * 
     * @return the last child
     * @see prefuse.data.Node#getLastChild() */
    @Override
    public Node getLastChild() {
        return m_graph.getSpanningTree().getLastChild(this);
    }

    /** Gets the previous sibling.
     * 
     * @return the previous sibling
     * @see prefuse.data.Node#getPreviousSibling() */
    @Override
    public Node getPreviousSibling() {
        return m_graph.getSpanningTree().getPreviousSibling(this);
    }

    /** Gets the next sibling.
     * 
     * @return the next sibling
     * @see prefuse.data.Node#getNextSibling() */
    @Override
    public Node getNextSibling() {
        return m_graph.getSpanningTree().getNextSibling(this);
    }

    /** Children.
     * 
     * @return the iterator
     * @see prefuse.data.Node#children() */
    @Override
    public Iterator children() {
        return m_graph.getSpanningTree().children(this);
    }

    /** Child edges.
     * 
     * @return the iterator
     * @see prefuse.data.Node#childEdges() */
    @Override
    public Iterator childEdges() {
        return m_graph.getSpanningTree().childEdges(this);
    }

    /** Gets the depth.
     * 
     * @return the depth
     * @see prefuse.data.Node#getDepth() */
    @Override
    public int getDepth() {
        return m_graph.getSpanningTree().getDepth(m_row);
    }
} // end of class TableNodeItem
