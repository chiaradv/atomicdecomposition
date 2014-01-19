package prefuse.util.collections;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Maintains a breadth-first-search queue as well as depth labels.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class Queue {

    // TODO: create an optimized implementation of this class
    
    /** The m_list. */
    private LinkedList m_list = new LinkedList();
    
    /** The m_map. */
    private HashMap    m_map  = new HashMap();
    
    /**
     * Clear.
     */
    public void clear() {
        m_list.clear();
        m_map.clear();
    }
    
    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return m_list.isEmpty();
    }
    
    /**
     * Adds the.
     *
     * @param o the o
     * @param depth the depth
     */
    public void add(Object o, int depth) {
        m_list.add(o);
        visit(o, depth);
    }
    
    /**
     * Gets the depth.
     *
     * @param o the o
     * @return the depth
     */
    public int getDepth(Object o) {
        Integer d = (Integer)m_map.get(o);
        return ( d==null ? -1 : d.intValue() );
    }
    
    /**
     * Visit.
     *
     * @param o the o
     * @param depth the depth
     */
    public void visit(Object o, int depth) {
        m_map.put(o, new Integer(depth));
    }
    
    /**
     * Removes the first.
     *
     * @return the object
     */
    public Object removeFirst() {
        return m_list.removeFirst();
    }
    
    /**
     * Removes the last.
     *
     * @return the object
     */
    public Object removeLast() {
        return m_list.removeLast();
    }
    
} // end of class Queue
