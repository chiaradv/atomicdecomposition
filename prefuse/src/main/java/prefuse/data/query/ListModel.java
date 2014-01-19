package prefuse.data.query;

import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import prefuse.util.collections.CopyOnWriteArrayList;


/**
 * List data model supporting both data modeling and selection management.
 * Though generally useful, this has been designed particularly to support
 * dynamic queries.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ListModel extends DefaultListSelectionModel
    implements MutableComboBoxModel
{
    
    /** The m_items. */
    private ArrayList m_items = new ArrayList();
    
    /** The m_lstnrs. */
    private CopyOnWriteArrayList m_lstnrs = new CopyOnWriteArrayList();
    
    /**
     * Create an empty ListModel.
     */
    public ListModel() {
        // do nothing
    }
    
    /**
     * Create a ListModel with the provided items.
     * @param items the items for the data model.
     */
    public ListModel(final Object[] items) {
        for ( int i=0; i<items.length; ++i )
            m_items.add(items[i]);
    }
    
    // --------------------------------------------------------------------
    
    /**
     * Indicates if the ListModel currently has multiple selections.
     * @return true if there are multiple selections, false otherwise
     */
    private boolean isMultipleSelection() {
        return getMaxSelectionIndex()-getMinSelectionIndex() > 0;
    }
    
    /**
     * Gets the selected item.
     *
     * @return the selected item
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    public Object getSelectedItem() {
        int idx = getMinSelectionIndex();
        return ( idx == -1 ? null : m_items.get(idx) );
    }
    
    /**
     * Sets the selected item.
     *
     * @param item the new selected item
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    public void setSelectedItem(Object item) {
        int idx = m_items.indexOf(item);
        if ( idx < 0 ) return;
        
        if ( !isMultipleSelection() && idx == getMinSelectionIndex() )
            return;
        
        super.setSelectionInterval(idx,idx);
        fireDataEvent(this,ListDataEvent.CONTENTS_CHANGED,-1,-1);
    }
    
    /**
     * Gets the size.
     *
     * @return the size
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return m_items.size();
    }
    
    /**
     * Gets the element at.
     *
     * @param idx the idx
     * @return the element at
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int idx) {
        return m_items.get(idx);
    }
    
    /**
     * Adds the element.
     *
     * @param item the item
     * @see javax.swing.MutableComboBoxModel#addElement(java.lang.Object)
     */
    public void addElement(Object item) {
        m_items.add(item);
        int sz = m_items.size()-1;
        fireDataEvent(this,ListDataEvent.INTERVAL_ADDED,sz,sz);
        if ( sz >= 0 && isSelectionEmpty() && item != null )
            setSelectedItem(item);
    }
    
    /**
     * Insert element at.
     *
     * @param item the item
     * @param idx the idx
     * @see javax.swing.MutableComboBoxModel#insertElementAt(java.lang.Object, int)
     */
    public void insertElementAt(Object item, int idx) {
        m_items.add(idx, item);
        fireDataEvent(this,ListDataEvent.INTERVAL_ADDED,idx,idx);
    }
    
    /**
     * Removes the element.
     *
     * @param item the item
     * @see javax.swing.MutableComboBoxModel#removeElement(java.lang.Object)
     */
    public void removeElement(Object item) {
        int idx = m_items.indexOf(item);
        if ( idx >= 0 )
            removeElementAt(idx);
    }
    
    /**
     * Removes the element at.
     *
     * @param idx the idx
     * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
     */
    public void removeElementAt(int idx) {
        if ( !isMultipleSelection() && idx == getMinSelectionIndex() ) {
            int sidx = ( idx==0 ? getSize()==1 ? -1 : idx+1 : idx-1 );
            Object sel = ( sidx == -1 ? null : m_items.get(sidx) );
            setSelectedItem(sel);
        }
    
        m_items.remove(idx);
        fireDataEvent(this,ListDataEvent.INTERVAL_REMOVED,idx,idx);
    }
    
    // --------------------------------------------------------------------
    // List Data Listeners
    
    /**
     * Adds the list data listener.
     *
     * @param l the l
     * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
     */
    public void addListDataListener(ListDataListener l) {
        if ( !m_lstnrs.contains(l) )
            m_lstnrs.add(l);
    }
    
    /**
     * Removes the list data listener.
     *
     * @param l the l
     * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
     */
    public void removeListDataListener(ListDataListener l) {
        m_lstnrs.remove(l);
    }
    
    /**
     * Fires a change notification in response to changes in the ListModel.
     *
     * @param src the src
     * @param type the type
     * @param idx0 the idx0
     * @param idx1 the idx1
     */
    protected void fireDataEvent(Object src, int type, int idx0, int idx1) {
        Object[] lstnrs = m_lstnrs.getArray();
        if ( lstnrs.length > 0 ) {
            ListDataEvent e = new ListDataEvent(src, type, idx0, idx1);
            for ( int i=0; i<lstnrs.length; ++i ) {
                ((ListDataListener)lstnrs[i]).contentsChanged(e);
            }
        }
    }

} // end of class ListModel
