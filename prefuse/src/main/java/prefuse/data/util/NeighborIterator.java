package prefuse.data.util;

import java.util.Iterator;

import prefuse.data.Edge;
import prefuse.data.Node;

/** Iterator over neighbors of a given Node. Resolves Edge instances to provide
 * direct iteration over the Node instances.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class NeighborIterator implements Iterator {
    /** The m_edges. */
    private final Iterator m_edges;
    /** The m_node. */
    private final Node m_node;

    /** Create a new NeighborIterator.
     * 
     * @param n
     *            the source node
     * @param edges
     *            the node edges to iterate over */
    public NeighborIterator(Node n, Iterator edges) {
        m_node = n;
        m_edges = edges;
    }

    /** Removes the.
     * 
     * @see java.util.Iterator#remove() */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /** Checks for next.
     * 
     * @return true, if successful
     * @see java.util.Iterator#hasNext() */
    @Override
    public boolean hasNext() {
        return m_edges.hasNext();
    }

    /** Next.
     * 
     * @return the object
     * @see java.util.Iterator#next() */
    @Override
    public Object next() {
        Edge e = (Edge) m_edges.next();
        return e.getAdjacentNode(m_node);
    }
} // end of class NeighborIterator
