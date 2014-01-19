package prefuse.data.tuple;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

/** Edge implementation that reads Edge data from a backing edge table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class TableEdge extends TableTuple implements Edge {
    /** The backing graph. */
    protected Graph m_graph;

    /** Initialize a new Edge backed by an edge table. This method is used by the
     * appropriate TupleManager instance, and should not be called directly by
     * client code, unless by a client-supplied custom TupleManager.
     * 
     * @param table
     *            the edge Table
     * @param graph
     *            the backing Graph
     * @param row
     *            the row in the edge table to which this Node instance
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
} // end of class TableEdge
