package prefuse.data.tuple;

import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.SwingPropertyChangeSupport;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.EventConstants;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Predicate;
import prefuse.data.util.FilterIteratorFactory;
import prefuse.data.util.Sort;
import prefuse.data.util.SortedTupleIterator;
import prefuse.util.collections.CopyOnWriteArrayList;

/** Abstract base class for TupleSet implementations. Provides mechanisms for
 * generating filtered tuple iterators, maintain listeners, and supporting bound
 * client properties.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractTupleSet implements TupleSet {
    /** Tuples.
     * 
     * @param filter
     *            the filter
     * @return the iterator
     * @see prefuse.data.tuple.TupleSet#tuples(prefuse.data.expression.Predicate) */
    @Override
    public Iterator tuples(Predicate filter) {
        if (filter == null) {
            return tuples();
        } else {
            return FilterIteratorFactory.tuples(this, filter);
        }
    }

    /** Tuples.
     * 
     * @param filter
     *            the filter
     * @param sort
     *            the sort
     * @return the iterator
     * @see prefuse.data.tuple.TupleSet#tuples(prefuse.data.expression.Predicate,
     *      prefuse.data.util.Sort) */
    @Override
    public Iterator tuples(Predicate filter, Sort sort) {
        if (sort == null) {
            return tuples(filter);
        } else {
            Comparator c = sort.getComparator(this);
            return new SortedTupleIterator(tuples(filter), getTupleCount(), c);
        }
    }

    // -- TupleSet Methods ----------------------------------------------------
    /** The m_tuple listeners. */
    private CopyOnWriteArrayList m_tupleListeners;

    /** Adds the tuple set listener.
     * 
     * @param tsl
     *            the tsl
     * @see prefuse.data.tuple.TupleSet#addTupleSetListener(prefuse.data.event.TupleSetListener) */
    @Override
    public void addTupleSetListener(TupleSetListener tsl) {
        if (m_tupleListeners == null) {
            m_tupleListeners = new CopyOnWriteArrayList();
        }
        if (!m_tupleListeners.contains(tsl)) {
            m_tupleListeners.add(tsl);
        }
    }

    /** Removes the tuple set listener.
     * 
     * @param tsl
     *            the tsl
     * @see prefuse.data.tuple.TupleSet#removeTupleSetListener(prefuse.data.event.TupleSetListener) */
    @Override
    public void removeTupleSetListener(TupleSetListener tsl) {
        if (m_tupleListeners != null) {
            m_tupleListeners.remove(tsl);
        }
    }

    /** Fire a Tuple event.
     * 
     * @param t
     *            the Table on which the event has occurred
     * @param start
     *            the first row changed
     * @param end
     *            the last row changed
     * @param type
     *            the type of event, one of
     *            {@link prefuse.data.event.EventConstants#INSERT} or
     *            {@link prefuse.data.event.EventConstants#DELETE}. */
    protected void fireTupleEvent(Table t, int start, int end, int type) {
        if (m_tupleListeners != null && m_tupleListeners.size() > 0) {
            Object[] lstnrs = m_tupleListeners.getArray();
            Tuple[] tuples = new Tuple[end - start + 1];
            for (int i = 0, r = start; r <= end; ++r, ++i) {
                tuples[i] = t.getTuple(r);
            }
            for (int i = 0; i < lstnrs.length; ++i) {
                TupleSetListener tsl = (TupleSetListener) lstnrs[i];
                if (type == EventConstants.INSERT) {
                    tsl.tupleSetChanged(this, tuples, EMPTY_ARRAY);
                } else {
                    tsl.tupleSetChanged(this, EMPTY_ARRAY, tuples);
                }
            }
        }
    }

    /** Fire a Tuple event.
     * 
     * @param t
     *            the tuple that has been added or removed
     * @param type
     *            the type of event, one of
     *            {@link prefuse.data.event.EventConstants#INSERT} or
     *            {@link prefuse.data.event.EventConstants#DELETE}. */
    protected void fireTupleEvent(Tuple t, int type) {
        if (m_tupleListeners != null && m_tupleListeners.size() > 0) {
            Object[] lstnrs = m_tupleListeners.getArray();
            Tuple[] ts = new Tuple[] { t };
            for (int i = 0; i < lstnrs.length; ++i) {
                TupleSetListener tsl = (TupleSetListener) lstnrs[i];
                if (type == EventConstants.INSERT) {
                    tsl.tupleSetChanged(this, ts, EMPTY_ARRAY);
                } else {
                    tsl.tupleSetChanged(this, EMPTY_ARRAY, ts);
                }
            }
        }
    }

    /** Fire a Tuple event.
     * 
     * @param added
     *            array of Tuples that have been added, can be null
     * @param removed
     *            array of Tuples that have been removed, can be null */
    protected void fireTupleEvent(Tuple[] added, Tuple[] removed) {
        if (m_tupleListeners != null && m_tupleListeners.size() > 0) {
            Object[] lstnrs = m_tupleListeners.getArray();
            added = added == null ? EMPTY_ARRAY : added;
            removed = removed == null ? EMPTY_ARRAY : removed;
            for (int i = 0; i < lstnrs.length; ++i) {
                TupleSetListener tsl = (TupleSetListener) lstnrs[i];
                tsl.tupleSetChanged(this, added, removed);
            }
        }
    }

    // -- Data Field Methods --------------------------------------------------
    /** False by default.
     * 
     * @return true, if is adds the column supported
     * @see prefuse.data.tuple.TupleSet#isAddColumnSupported() */
    @Override
    public boolean isAddColumnSupported() {
        return false;
    }

    /** Adds the columns.
     * 
     * @param schema
     *            the schema
     * @see prefuse.data.tuple.TupleSet#addColumns(prefuse.data.Schema) */
    @Override
    public void addColumns(Schema schema) {
        if (isAddColumnSupported()) {
            for (int i = 0; i < schema.getColumnCount(); ++i) {
                try {
                    addColumn(schema.getColumnName(i), schema.getColumnType(i),
                            schema.getDefault(i));
                } catch (IllegalArgumentException iae) {}
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Unsupported by default.
     * 
     * @param name
     *            the name
     * @param type
     *            the type
     * @param defaultValue
     *            the default value
     * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String,
     *      java.lang.Class, java.lang.Object) */
    @Override
    public void addColumn(String name, Class type, Object defaultValue) {
        throw new UnsupportedOperationException();
    }

    /** Unsupported by default.
     * 
     * @param name
     *            the name
     * @param type
     *            the type
     * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String,
     *      java.lang.Class) */
    @Override
    public void addColumn(String name, Class type) {
        throw new UnsupportedOperationException();
    }

    /** Unsupported by default.
     * 
     * @param name
     *            the name
     * @param expr
     *            the expr
     * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String,
     *      prefuse.data.expression.Expression) */
    @Override
    public void addColumn(String name, Expression expr) {
        throw new UnsupportedOperationException();
    }

    /** Unsupported by default.
     * 
     * @param name
     *            the name
     * @param expr
     *            the expr
     * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String,
     *      java.lang.String) */
    @Override
    public void addColumn(String name, String expr) {
        throw new UnsupportedOperationException();
    }

    // -- Client Properties ---------------------------------------------------
    /** The m_props. */
    private HashMap m_props;
    /** The m_prop support. */
    private SwingPropertyChangeSupport m_propSupport;

    /** Adds the property change listener.
     * 
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.tuple.TupleSet#addPropertyChangeListener(java.beans.PropertyChangeListener) */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener lstnr) {
        if (lstnr == null) {
            return;
        }
        if (m_propSupport == null) {
            m_propSupport = new SwingPropertyChangeSupport(this);
        }
        m_propSupport.addPropertyChangeListener(lstnr);
    }

    /** Adds the property change listener.
     * 
     * @param key
     *            the key
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.tuple.TupleSet#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener) */
    @Override
    public void addPropertyChangeListener(String key, PropertyChangeListener lstnr) {
        if (lstnr == null) {
            return;
        }
        if (m_propSupport == null) {
            m_propSupport = new SwingPropertyChangeSupport(this);
        }
        m_propSupport.addPropertyChangeListener(key, lstnr);
    }

    /** Removes the property change listener.
     * 
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.tuple.TupleSet#removePropertyChangeListener(java.beans.PropertyChangeListener) */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener lstnr) {
        if (lstnr == null) {
            return;
        }
        if (m_propSupport == null) {
            return;
        }
        m_propSupport.removePropertyChangeListener(lstnr);
    }

    /** Removes the property change listener.
     * 
     * @param key
     *            the key
     * @param lstnr
     *            the lstnr
     * @see prefuse.data.tuple.TupleSet#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener) */
    @Override
    public void removePropertyChangeListener(String key, PropertyChangeListener lstnr) {
        if (lstnr == null) {
            return;
        }
        if (m_propSupport == null) {
            return;
        }
        m_propSupport.removePropertyChangeListener(key, lstnr);
    }

    /** Put client property.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @see prefuse.data.tuple.TupleSet#putClientProperty(java.lang.String,
     *      java.lang.Object) */
    @Override
    public void putClientProperty(String key, Object value) {
        Object prev = null;
        if (m_props == null && value == null) {
            // nothing to do
            return;
        } else if (value == null) {
            prev = m_props.remove(key);
        } else {
            if (m_props == null) {
                m_props = new HashMap(2);
            }
            prev = m_props.put(key, value);
        }
        if (m_propSupport != null) {
            m_propSupport.firePropertyChange(key, prev, value);
        }
    }

    /** Gets the client property.
     * 
     * @param key
     *            the key
     * @return the client property
     * @see prefuse.data.tuple.TupleSet#getClientProperty(java.lang.String) */
    @Override
    public Object getClientProperty(String key) {
        return m_props == null ? null : m_props.get(key);
    }
} // end of class AbstractTupleSet
