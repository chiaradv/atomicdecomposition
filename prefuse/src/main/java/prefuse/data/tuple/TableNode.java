package prefuse.data.tuple;

import java.util.Iterator;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;


/**
 * Node implementation that reads Node data from a backing node table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableNode extends TableTuple implements Node {

    /**
     * The backing graph.
     */
    protected Graph m_graph;
    
    /**
     * Initialize a new Node backed by a node table. This method is used by
     * the appropriate TupleManager instance, and should not be called
     * directly by client code, unless by a client-supplied custom
     * TupleManager.
     * @param table the node Table
     * @param graph the backing Graph
     * @param row the row in the node table to which this Node instance
     *  corresponds.
     */
    protected void init(Table table, Graph graph, int row) {
        m_table = table;
        m_graph = graph;
        m_row = m_table.isValidRow(row) ? row : -1;
    }
    
    /**
     * Gets the graph.
     *
     * @return the graph
     * @see prefuse.data.Node#getGraph()
     */
    public Graph getGraph() {
        return m_graph;
    }
    
    // ------------------------------------------------------------------------
    // Graph Methods
    
    /**
     * Gets the in degree.
     *
     * @return the in degree
     * @see prefuse.data.Node#getInDegree()
     */
    public int getInDegree() {
        return m_graph.getInDegree(this);
    }

    /**
     * Gets the out degree.
     *
     * @return the out degree
     * @see prefuse.data.Node#getOutDegree()
     */
    public int getOutDegree() {
        return m_graph.getOutDegree(this);
    }

    /**
     * Gets the degree.
     *
     * @return the degree
     * @see prefuse.data.Node#getDegree()
     */
    public int getDegree() {
        return m_graph.getDegree(this);
    }

    /**
     * In edges.
     *
     * @return the iterator
     * @see prefuse.data.Node#inEdges()
     */
    public Iterator inEdges() {
        return m_graph.inEdges(this);
    }

    /**
     * Out edges.
     *
     * @return the iterator
     * @see prefuse.data.Node#outEdges()
     */
    public Iterator outEdges() {
        return m_graph.outEdges(this);
    }

    /**
     * Edges.
     *
     * @return the iterator
     * @see prefuse.data.Node#edges()
     */
    public Iterator edges() {
        return m_graph.edges(this);
    }
    
    /**
     * In neighbors.
     *
     * @return the iterator
     * @see prefuse.data.Node#inNeighbors()
     */
    public Iterator inNeighbors() {
        return m_graph.inNeighbors(this);
    }
    
    /**
     * Out neighbors.
     *
     * @return the iterator
     * @see prefuse.data.Node#outNeighbors()
     */
    public Iterator outNeighbors() {
        return m_graph.outNeighbors(this);
    }
    
    /**
     * Neighbors.
     *
     * @return the iterator
     * @see prefuse.data.Node#neighbors()
     */
    public Iterator neighbors() {
        return m_graph.neighbors(this);
    }

    
    // ------------------------------------------------------------------------
    // Tree Methods

    /**
     * Gets the parent.
     *
     * @return the parent
     * @see prefuse.data.Node#getParent()
     */
    public Node getParent() {
        return m_graph.getSpanningTree().getParent(this);
    }

    /**
     * Gets the parent edge.
     *
     * @return the parent edge
     * @see prefuse.data.Node#getParentEdge()
     */
    public Edge getParentEdge() {
        return m_graph.getSpanningTree().getParentEdge(this);
    }
    
    /**
     * Gets the child count.
     *
     * @return the child count
     * @see prefuse.data.Node#getChildCount()
     */
    public int getChildCount() {
        return m_graph.getSpanningTree().getChildCount(m_row);
    }

    /**
     * Gets the child index.
     *
     * @param child the child
     * @return the child index
     * @see prefuse.data.Node#getChildIndex(prefuse.data.Node)
     */
    public int getChildIndex(Node child) {
        return m_graph.getSpanningTree().getChildIndex(this, child);
    }
    
    /**
     * Gets the child.
     *
     * @param idx the idx
     * @return the child
     * @see prefuse.data.Node#getChild(int)
     */
    public Node getChild(int idx) {
        return m_graph.getSpanningTree().getChild(this, idx);
    }
    
    /**
     * Gets the first child.
     *
     * @return the first child
     * @see prefuse.data.Node#getFirstChild()
     */
    public Node getFirstChild() {
        return m_graph.getSpanningTree().getFirstChild(this);
    }
    
    /**
     * Gets the last child.
     *
     * @return the last child
     * @see prefuse.data.Node#getLastChild()
     */
    public Node getLastChild() {
        return m_graph.getSpanningTree().getLastChild(this);
    }
    
    /**
     * Gets the previous sibling.
     *
     * @return the previous sibling
     * @see prefuse.data.Node#getPreviousSibling()
     */
    public Node getPreviousSibling() {
        return m_graph.getSpanningTree().getPreviousSibling(this);
    }
    
    /**
     * Gets the next sibling.
     *
     * @return the next sibling
     * @see prefuse.data.Node#getNextSibling()
     */
    public Node getNextSibling() {
        return m_graph.getSpanningTree().getNextSibling(this);
    }
    
    /**
     * Children.
     *
     * @return the iterator
     * @see prefuse.data.Node#children()
     */
    public Iterator children() {
        return m_graph.getSpanningTree().children(this);
    }

    /**
     * Child edges.
     *
     * @return the iterator
     * @see prefuse.data.Node#childEdges()
     */
    public Iterator childEdges() {
        return m_graph.getSpanningTree().childEdges(this);
    }

    /**
     * Gets the depth.
     *
     * @return the depth
     * @see prefuse.data.Node#getDepth()
     */
    public int getDepth() {
        return m_graph.getSpanningTree().getDepth(m_row);
    }

} // end of class TableNode
