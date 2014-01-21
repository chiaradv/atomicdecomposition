package prefuse.data.util;

import java.util.Comparator;

import prefuse.data.Table;
import prefuse.data.column.Column;
import prefuse.data.event.ColumnListener;
import prefuse.data.event.EventConstants;
import prefuse.data.event.TableListener;
import prefuse.util.collections.BooleanIntSortedMap;
import prefuse.util.collections.DoubleIntSortedMap;
import prefuse.util.collections.FloatIntSortedMap;
import prefuse.util.collections.IncompatibleComparatorException;
import prefuse.util.collections.IntIntSortedMap;
import prefuse.util.collections.IntIterator;
import prefuse.util.collections.IntSortedMap;
import prefuse.util.collections.LongIntSortedMap;
import prefuse.util.collections.ObjectIntSortedMap;
import prefuse.util.collections.SortedMapFactory;

/** Index instance that uses red-black trees to provide an index over a column of
 * data.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class TreeIndex implements Index, ColumnListener, TableListener {
    /** The m_table. */
    protected Table m_table;
    /** The m_rows. */
    protected RowManager m_rows;
    /** The m_col. */
    protected Column m_col;
    /** The m_index. */
    protected IntSortedMap m_index;
    /** The m_reindex. */
    protected boolean m_reindex;
    /** The m_colidx. */
    protected int m_colidx;

    /** Create a new TreeIndex.
     * 
     * @param t
     *            the Table containing the data column to index
     * @param rows
     *            the RowManager of the Table
     * @param col
     *            the Column instance to index
     * @param cmp
     *            the Comparator to use to sort data values
     * @throws IncompatibleComparatorException
     *             if the comparator is not compatible with the column's data
     *             type */
    public TreeIndex(Table t, RowManager rows, Column col, Comparator cmp)
            throws IncompatibleComparatorException {
        m_table = t;
        m_rows = rows;
        m_col = col;
        m_index = SortedMapFactory.getMap(col.getColumnType(), cmp, false);
        index();
        m_col.addColumnListener(this);
        m_table.addTableListener(this);
    }

    /** Dispose.
     * 
     * @see prefuse.data.util.Index#dispose() */
    @Override
    public void dispose() {
        m_col.removeColumnListener(this);
        m_table.removeTableListener(this);
    }

    /** Gets the comparator.
     * 
     * @return the comparator
     * @see prefuse.data.util.Index#getComparator() */
    @Override
    public Comparator getComparator() {
        return m_index.comparator();
    }

    /** Size.
     * 
     * @return the int
     * @see prefuse.data.util.Index#size() */
    @Override
    public int size() {
        return m_index.size();
    }

    /** Gets the column index.
     * 
     * @return the column index */
    private int getColumnIndex() {
        if (!(m_table.getColumn(m_colidx) == m_col)) {
            m_colidx = m_table.getColumnNumber(m_col);
        }
        return m_colidx;
    }

    // ------------------------------------------------------------------------
    // Index Update Methods
    /** Index.
     * 
     * @see prefuse.data.util.Index#index() */
    @Override
    public void index() {
        m_index.clear();
        // iterate over all valid values, adding them to the index
        int idx = getColumnIndex();
        m_colidx = idx;
        IntIterator rows = m_rows.rows();
        if (m_index instanceof IntIntSortedMap) {
            IntIntSortedMap map = (IntIntSortedMap) m_index;
            while (rows.hasNext()) {
                int r = rows.nextInt();
                map.put(m_col.getInt(m_table.getColumnRow(r, idx)), r);
            }
        } else if (m_index instanceof LongIntSortedMap) {
            LongIntSortedMap map = (LongIntSortedMap) m_index;
            while (rows.hasNext()) {
                int r = rows.nextInt();
                map.put(m_col.getLong(m_table.getColumnRow(r, idx)), r);
            }
        } else if (m_index instanceof FloatIntSortedMap) {
            FloatIntSortedMap map = (FloatIntSortedMap) m_index;
            while (rows.hasNext()) {
                int r = rows.nextInt();
                map.put(m_col.getFloat(m_table.getColumnRow(r, idx)), r);
            }
        } else if (m_index instanceof DoubleIntSortedMap) {
            DoubleIntSortedMap map = (DoubleIntSortedMap) m_index;
            while (rows.hasNext()) {
                int r = rows.nextInt();
                map.put(m_col.getDouble(m_table.getColumnRow(r, idx)), r);
            }
        } else if (m_index instanceof BooleanIntSortedMap) {
            BooleanIntSortedMap map = (BooleanIntSortedMap) m_index;
            while (rows.hasNext()) {
                int r = rows.nextInt();
                map.put(m_col.getBoolean(m_table.getColumnRow(r, idx)), r);
            }
        } else if (m_index instanceof ObjectIntSortedMap) {
            ObjectIntSortedMap map = (ObjectIntSortedMap) m_index;
            while (rows.hasNext()) {
                int r = rows.nextInt();
                map.put(m_col.get(m_table.getColumnRow(r, idx)), r);
            }
        } else {
            throw new IllegalStateException();
        }
        m_reindex = false;
    }

    // ------------------------------------------------------------------------
    // Listener Methods
    /** Table changed.
     * 
     * @param t
     *            the t
     * @param start
     *            the start
     * @param end
     *            the end
     * @param col
     *            the col
     * @param type
     *            the type
     * @see prefuse.data.event.TableListener#tableChanged(prefuse.data.Table,
     *      int, int, int, int) */
    @Override
    public void tableChanged(Table t, int start, int end, int col, int type) {
        if (type == EventConstants.UPDATE || t != m_table
                || col != EventConstants.ALL_COLUMNS) {
            return;
        }
        boolean insert = type == EventConstants.INSERT;
        for (int r = start; r <= end; ++r) {
            rowChanged(r, insert);
        }
    }

    /** Row changed.
     * 
     * @param row
     *            the row
     * @param insert
     *            the insert */
    private void rowChanged(int row, boolean insert) {
        // make sure we access the right column value
        int crow = m_rows.getColumnRow(row, getColumnIndex());
        if (m_index instanceof IntIntSortedMap) {
            IntIntSortedMap map = (IntIntSortedMap) m_index;
            int key = m_col.getInt(row);
            if (insert) {
                map.put(key, row);
            } else {
                map.remove(key, row);
            }
        } else if (m_index instanceof LongIntSortedMap) {
            LongIntSortedMap map = (LongIntSortedMap) m_index;
            long key = m_col.getLong(crow);
            if (insert) {
                map.put(key, row);
            } else {
                map.remove(key, row);
            }
        } else if (m_index instanceof FloatIntSortedMap) {
            FloatIntSortedMap map = (FloatIntSortedMap) m_index;
            float key = m_col.getFloat(crow);
            if (insert) {
                map.put(key, row);
            } else {
                map.remove(key, row);
            }
        } else if (m_index instanceof DoubleIntSortedMap) {
            DoubleIntSortedMap map = (DoubleIntSortedMap) m_index;
            double key = m_col.getDouble(crow);
            if (insert) {
                map.put(key, row);
            } else {
                map.remove(key, row);
            }
        } else if (m_index instanceof BooleanIntSortedMap) {
            BooleanIntSortedMap map = (BooleanIntSortedMap) m_index;
            boolean key = m_col.getBoolean(crow);
            if (insert) {
                map.put(key, row);
            } else {
                map.remove(key, row);
            }
        } else if (m_index instanceof ObjectIntSortedMap) {
            ObjectIntSortedMap map = (ObjectIntSortedMap) m_index;
            Object key = m_col.get(crow);
            if (insert) {
                map.put(key, row);
            } else {
                map.remove(key, row);
            }
        } else {
            throw new IllegalStateException();
        }
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param type
     *            the type
     * @param start
     *            the start
     * @param end
     *            the end
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, int, int) */
    @Override
    public void columnChanged(Column src, int type, int start, int end) {
        m_reindex = true;
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param idx
     *            the idx
     * @param prev
     *            the prev
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, boolean) */
    @Override
    public void columnChanged(Column src, int idx, boolean prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if (row < 0) {
            return; // invalid row value
        }
        ((BooleanIntSortedMap) m_index).remove(prev, row);
        ((BooleanIntSortedMap) m_index).put(src.getBoolean(idx), row);
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param idx
     *            the idx
     * @param prev
     *            the prev
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, int) */
    @Override
    public void columnChanged(Column src, int idx, int prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if (row < 0) {
            return; // invalid row value
        }
        ((IntIntSortedMap) m_index).remove(prev, row);
        ((IntIntSortedMap) m_index).put(src.getInt(idx), row);
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param idx
     *            the idx
     * @param prev
     *            the prev
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, long) */
    @Override
    public void columnChanged(Column src, int idx, long prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if (row < 0) {
            return; // invalid row value
        }
        ((LongIntSortedMap) m_index).remove(prev, row);
        ((LongIntSortedMap) m_index).put(src.getLong(idx), row);
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param idx
     *            the idx
     * @param prev
     *            the prev
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, float) */
    @Override
    public void columnChanged(Column src, int idx, float prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if (row < 0) {
            return; // invalid row value
        }
        ((FloatIntSortedMap) m_index).remove(prev, row);
        ((FloatIntSortedMap) m_index).put(src.getFloat(idx), row);
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param idx
     *            the idx
     * @param prev
     *            the prev
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, double) */
    @Override
    public void columnChanged(Column src, int idx, double prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if (row < 0) {
            return; // invalid row value
        }
        ((DoubleIntSortedMap) m_index).remove(prev, row);
        ((DoubleIntSortedMap) m_index).put(src.getDouble(idx), row);
    }

    /** Column changed.
     * 
     * @param src
     *            the src
     * @param idx
     *            the idx
     * @param prev
     *            the prev
     * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column,
     *      int, java.lang.Object) */
    @Override
    public void columnChanged(Column src, int idx, Object prev) {
        int row = m_rows.getTableRow(idx, getColumnIndex());
        if (row < 0) {
            return; // invalid row value
        }
        ((ObjectIntSortedMap) m_index).remove(prev, row);
        ((ObjectIntSortedMap) m_index).put(src.get(idx), row);
    }

    // ------------------------------------------------------------------------
    // Retrieval Methods
    /** Minimum.
     * 
     * @return the int
     * @see prefuse.data.util.Index#minimum() */
    @Override
    public int minimum() {
        return m_index.getMinimum();
    }

    /** Maximum.
     * 
     * @return the int
     * @see prefuse.data.util.Index#maximum() */
    @Override
    public int maximum() {
        return m_index.getMaximum();
    }

    /** Median.
     * 
     * @return the int
     * @see prefuse.data.util.Index#median() */
    @Override
    public int median() {
        return m_index.getMedian();
    }

    /** Unique count.
     * 
     * @return the int
     * @see prefuse.data.util.Index#uniqueCount() */
    @Override
    public int uniqueCount() {
        return m_index.getUniqueCount();
    }

    // ------------------------------------------------------------------------
    /** All rows.
     * 
     * @param type
     *            the type
     * @return the int iterator
     * @see prefuse.data.util.Index#allRows(int) */
    @Override
    public IntIterator allRows(int type) {
        boolean ascending = (type & Index.TYPE_ASCENDING) > 0;
        return m_index.valueIterator(ascending);
    }

    /** Rows.
     * 
     * @param lo
     *            the lo
     * @param hi
     *            the hi
     * @param type
     *            the type
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(java.lang.Object, java.lang.Object,
     *      int) */
    @Override
    public IntIterator rows(Object lo, Object hi, int type) {
        if (!(m_index instanceof ObjectIntSortedMap)) {
            throw new IllegalStateException();
        }
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        if (lo == null) {
            lo = ObjectIntSortedMap.MIN_KEY;
        }
        if (hi == null) {
            hi = ObjectIntSortedMap.MAX_KEY;
        }
        ObjectIntSortedMap index = (ObjectIntSortedMap) m_index;
        if (reverse) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }

    /** Rows.
     * 
     * @param lo
     *            the lo
     * @param hi
     *            the hi
     * @param type
     *            the type
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(int, int, int) */
    @Override
    public IntIterator rows(int lo, int hi, int type) {
        if (!(m_index instanceof IntIntSortedMap)) {
            throw new IllegalStateException();
        }
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        IntIntSortedMap index = (IntIntSortedMap) m_index;
        if (reverse) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }

    /** Rows.
     * 
     * @param lo
     *            the lo
     * @param hi
     *            the hi
     * @param type
     *            the type
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(long, long, int) */
    @Override
    public IntIterator rows(long lo, long hi, int type) {
        if (!(m_index instanceof LongIntSortedMap)) {
            throw new IllegalStateException();
        }
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        LongIntSortedMap index = (LongIntSortedMap) m_index;
        if (reverse) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }

    /** Rows.
     * 
     * @param lo
     *            the lo
     * @param hi
     *            the hi
     * @param type
     *            the type
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(float, float, int) */
    @Override
    public IntIterator rows(float lo, float hi, int type) {
        if (!(m_index instanceof FloatIntSortedMap)) {
            throw new IllegalStateException();
        }
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        FloatIntSortedMap index = (FloatIntSortedMap) m_index;
        if (reverse) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }

    /** Rows.
     * 
     * @param lo
     *            the lo
     * @param hi
     *            the hi
     * @param type
     *            the type
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(double, double, int) */
    @Override
    public IntIterator rows(double lo, double hi, int type) {
        if (!(m_index instanceof DoubleIntSortedMap)) {
            throw new IllegalStateException();
        }
        boolean reverse = (type & Index.TYPE_DESCENDING) > 0;
        boolean linc = (type & Index.TYPE_LEFT_INCLUSIVE) > 0;
        boolean hinc = (type & Index.TYPE_RIGHT_INCLUSIVE) > 0;
        DoubleIntSortedMap index = (DoubleIntSortedMap) m_index;
        if (reverse) {
            return index.valueRangeIterator(hi, hinc, lo, linc);
        } else {
            return index.valueRangeIterator(lo, linc, hi, hinc);
        }
    }

    // ------------------------------------------------------------------------
    /** Rows.
     * 
     * @param val
     *            the val
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(int) */
    @Override
    public IntIterator rows(int val) {
        return rows(val, val, Index.TYPE_AII);
    }

    /** Rows.
     * 
     * @param val
     *            the val
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(long) */
    @Override
    public IntIterator rows(long val) {
        return rows(val, val, Index.TYPE_AII);
    }

    /** Rows.
     * 
     * @param val
     *            the val
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(float) */
    @Override
    public IntIterator rows(float val) {
        return rows(val, val, Index.TYPE_AII);
    }

    /** Rows.
     * 
     * @param val
     *            the val
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(double) */
    @Override
    public IntIterator rows(double val) {
        return rows(val, val, Index.TYPE_AII);
    }

    /** Rows.
     * 
     * @param val
     *            the val
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(boolean) */
    @Override
    public IntIterator rows(boolean val) {
        if (!(m_index instanceof BooleanIntSortedMap)) {
            throw new IllegalStateException();
        }
        BooleanIntSortedMap index = (BooleanIntSortedMap) m_index;
        return index.valueRangeIterator(val, true, val, true);
    }

    /** Rows.
     * 
     * @param val
     *            the val
     * @return the int iterator
     * @see prefuse.data.util.Index#rows(java.lang.Object) */
    @Override
    public IntIterator rows(Object val) {
        return rows(val, val, Index.TYPE_AII);
    }

    // ------------------------------------------------------------------------
    /** Gets the.
     * 
     * @param x
     *            the x
     * @return the int
     * @see prefuse.data.util.Index#get(double) */
    @Override
    public int get(double x) {
        DoubleIntSortedMap index = (DoubleIntSortedMap) m_index;
        return index.get(x);
    }

    /** Gets the.
     * 
     * @param x
     *            the x
     * @return the int
     * @see prefuse.data.util.Index#get(float) */
    @Override
    public int get(float x) {
        FloatIntSortedMap index = (FloatIntSortedMap) m_index;
        return index.get(x);
    }

    /** Gets the.
     * 
     * @param x
     *            the x
     * @return the int
     * @see prefuse.data.util.Index#get(int) */
    @Override
    public int get(int x) {
        IntIntSortedMap index = (IntIntSortedMap) m_index;
        return index.get(x);
    }

    /** Gets the.
     * 
     * @param x
     *            the x
     * @return the int
     * @see prefuse.data.util.Index#get(long) */
    @Override
    public int get(long x) {
        LongIntSortedMap index = (LongIntSortedMap) m_index;
        return index.get(x);
    }

    /** Gets the.
     * 
     * @param x
     *            the x
     * @return the int
     * @see prefuse.data.util.Index#get(java.lang.Object) */
    @Override
    public int get(Object x) {
        ObjectIntSortedMap index = (ObjectIntSortedMap) m_index;
        return index.get(x);
    }
} // end of class ColumnIndex
