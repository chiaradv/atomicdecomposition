package prefuse.data.search;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import prefuse.data.Tuple;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.util.StringLib;

/** SearchTupleSet implementation that treats the query as a regular expression
 * to match against all indexed Tuple data fields. The regular expression engine
 * provided by the standard Java libraries ({@link java.util.regex.Pattern
 * java.util.regex.Pattern}) is used; please refer to the documentation for that
 * class for more about the regular expression syntax.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see prefuse.data.query.SearchQueryBinding */
public class RegexSearchTupleSet extends SearchTupleSet {
    /** The m_query. */
    private String m_query = "";
    /** The m_case sensitive. */
    private final boolean m_caseSensitive;
    /** The m_source. */
    private final LinkedHashMap m_source = new LinkedHashMap();

    /** Create a new, case-insensitive regular expression search tuple set. */
    public RegexSearchTupleSet() {
        this(false);
    }

    /** Create a new regular expression search tuple set.
     * 
     * @param caseSensitive
     *            true to make the indexing case sensitive, false otherwise. */
    public RegexSearchTupleSet(boolean caseSensitive) {
        m_caseSensitive = caseSensitive;
    }

    /** Gets the query.
     * 
     * @return the query
     * @see prefuse.data.search.SearchTupleSet#getQuery() */
    @Override
    public String getQuery() {
        return m_query;
    }

    /** Search.
     * 
     * @param query
     *            the query
     * @see prefuse.data.search.SearchTupleSet#search(java.lang.String) */
    @Override
    public void search(String query) {
        if (query == null) {
            query = "";
        }
        if (!m_caseSensitive) {
            query = query.toLowerCase();
        }
        if (query.equals(m_query)) {
            return;
        }
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(query);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.warning("Pattern compile failed." + "\n" + StringLib.getStackTrace(e));
            return;
        }
        Tuple[] rem = clearInternal();
        m_query = query;
        Iterator fields = m_source.keySet().iterator();
        while (fields.hasNext()) {
            String field = (String) fields.next();
            TupleSet ts = (TupleSet) m_source.get(field);
            Iterator tuples = ts.tuples();
            while (tuples.hasNext()) {
                Tuple t = (Tuple) tuples.next();
                String text = t.getString(field);
                if (!m_caseSensitive) {
                    text = text.toLowerCase();
                }
                if (pattern.matcher(text).matches()) {
                    addInternal(t);
                }
            }
        }
        Tuple[] add = getTupleCount() > 0 ? toArray() : null;
        fireTupleEvent(add, rem);
    }

    /** Index.
     * 
     * @param t
     *            the t
     * @param field
     *            the field
     * @see prefuse.data.search.SearchTupleSet#index(prefuse.data.Tuple,
     *      java.lang.String) */
    @Override
    public void index(Tuple t, String field) {
        TupleSet ts = (TupleSet) m_source.get(field);
        if (ts == null) {
            ts = new DefaultTupleSet();
            m_source.put(field, ts);
        }
        ts.addTuple(t);
    }

    /** Unindex.
     * 
     * @param t
     *            the t
     * @param field
     *            the field
     * @see prefuse.data.search.SearchTupleSet#unindex(prefuse.data.Tuple,
     *      java.lang.String) */
    @Override
    public void unindex(Tuple t, String field) {
        TupleSet ts = (TupleSet) m_source.get(field);
        if (ts != null) {
            ts.removeTuple(t);
        }
    }

    /** Returns true, as unidexing is supported by this class.
     * 
     * @return true, if is unindex supported
     * @see prefuse.data.search.SearchTupleSet#isUnindexSupported() */
    @Override
    public boolean isUnindexSupported() {
        return true;
    }

    /** Removes all search hits and clears out the index.
     * 
     * @see prefuse.data.tuple.TupleSet#clear() */
    @Override
    public void clear() {
        m_source.clear();
        super.clear();
    }
} // end of class RegexSearchTupleSet
