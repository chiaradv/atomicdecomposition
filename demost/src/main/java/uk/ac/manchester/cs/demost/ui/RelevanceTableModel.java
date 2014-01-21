package uk.ac.manchester.cs.demost.ui;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** The Class RelevanceTableModel.
 * 
 * @param <T>
 *            the generic type */
public class RelevanceTableModel<T> {
    /** The rows. */
    private final List<Row<T>> rows = new ArrayList<Row<T>>();
    /** The headings. */
    String[] headings = new String[] {};

    /** Gets the headings.
     * 
     * @return the headings */
    public String[] getHeadings() {
        return headings;
    }

    /** Sets the headings.
     * 
     * @param headings
     *            the new headings */
    public void setHeadings(String[] headings) {
        this.headings = headings;
    }

    /** Adds the record.
     * 
     * @param record
     *            the record */
    public void addRecord(Row<T> record) {
        rows.add(record);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < headings.length; i++) {
            s.append(headings[i]);
            s.append(Row.CSV_SEPARATOR);
        }
        s.append('\n');
        for (Row<T> r : rows) {
            s.append(r);
            s.append("\n");
        }
        return s.toString();
    }

    /** To string.
     * 
     * @param l
     *            the l
     * @return the string */
    public String toString(List<?> l) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < headings.length; i++) {
            s.append(headings[i]);
            s.append(Row.CSV_SEPARATOR);
        }
        s.append('\n');
        for (Row<T> r : rows) {
            s.append(r.toString(l));
            s.append("\n");
        }
        return s.toString();
    }

    /** To string.
     * 
     * @param l
     *            the l
     * @param p
     *            the p */
    public void toString(Renderer<T> l, PrintStream p) {
        for (int i = 0; i < headings.length; i++) {
            p.print(headings[i]);
            p.print(Row.CSV_SEPARATOR);
        }
        p.print('\n');
        for (Row<T> r : rows) {
            p.print(r.toString(l));
            p.print("\n");
        }
    }

    // public void sort(final int i) {
    // Comparator<Record> comp = null;
    // if (i == 0) {
    // comp = new Comparator<Record>() {
    // @Override
    // public int compare(Record o1, Record o2) {
    // return o1.entity.compareTo(o2.entity);
    // }
    // };
    // } else {
    // comp = new Comparator<Record>() {
    // public int compare(Record o1, Record o2) {
    // return o2.getValue(i - 1) - o1.getValue(i - 1);
    // }
    // };
    // }
    // Collections.sort(records, comp);
    // }
    /** Gets the records.
     * 
     * @return the records */
    public List<Row<T>> getRecords() {
        return new ArrayList<Row<T>>(rows);
    }

    /** Gets the record.
     * 
     * @param rowIndex
     *            the row index
     * @return the record */
    public Row<T> getRecord(int rowIndex) {
        return rows.get(rowIndex);
    }

    /** Gets the record size.
     * 
     * @return the record size */
    public int getRecordSize() {
        return rows.size();
    }
}
