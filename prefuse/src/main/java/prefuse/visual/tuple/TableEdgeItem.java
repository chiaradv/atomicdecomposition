package prefuse.visual.tuple;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;

/** EdgeItem implementation that used data values from a backing VisualTable of
 * edges.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class TableEdgeItem extends TableVisualItem implements EdgeItem {
    /** The m_graph. */
    protected Graph m_graph;

    /** Initialize a new TableEdgeItem for the given graph, table, and row. This
     * method is used by the appropriate TupleManager instance, and should not
     * be called directly by client code, unless by a client-supplied custom
     * TupleManager.
     * 
     * @param table
     *            the backing VisualTable
     * @param graph
     *            the backing VisualGraph
     * @param row
     *            the row in the node table to which this Edge instance
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
     * @see prefuse.data.Edge#getGraph() */
    @Override
    public Graph getGraph() {
        return m_graph;
    }

    /** Checks if is directed.
     * 
     * @return true, if is directed
     * @see prefuse.data.Edge#isDirected() */
    @Override
    public boolean isDirected() {
        return m_graph.isDirected();
    }

    /** Gets the source node.
     * 
     * @return the source node
     * @see prefuse.data.Edge#getSourceNode() */
    @Override
    public Node getSourceNode() {
        return m_graph.getSourceNode(this);
    }

    /** Gets the target node.
     * 
     * @return the target node
     * @see prefuse.data.Edge#getTargetNode() */
    @Override
    public Node getTargetNode() {
        return m_graph.getTargetNode(this);
    }

    /** Gets the adjacent node.
     * 
     * @param n
     *            the n
     * @return the adjacent node
     * @see prefuse.data.Edge#getAdjacentNode(prefuse.data.Node) */
    @Override
    public Node getAdjacentNode(Node n) {
        return m_graph.getAdjacentNode(this, n);
    }

    /** Gets the source item.
     * 
     * @return the source item
     * @see prefuse.visual.EdgeItem#getSourceItem() */
    @Override
    public NodeItem getSourceItem() {
        return (NodeItem) getSourceNode();
    }

    /** Gets the target item.
     * 
     * @return the target item
     * @see prefuse.visual.EdgeItem#getTargetItem() */
    @Override
    public NodeItem getTargetItem() {
        return (NodeItem) getTargetNode();
    }

    /** Gets the adjacent item.
     * 
     * @param n
     *            the n
     * @return the adjacent item
     * @see prefuse.visual.EdgeItem#getAdjacentItem(prefuse.visual.NodeItem) */
    @Override
    public NodeItem getAdjacentItem(NodeItem n) {
        return (NodeItem) getAdjacentNode(n);
    }
} // end of class TableEdgeItem
