package uk.ac.manchester.cs.demost.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** The Class InfluenceMatrixModel. */
public class InfluenceMatrixModel {
    /** The records. */
    private final List<Record> records = new ArrayList<Record>();
    /** The headings. */
    String[] headings = new String[] { "Entity", "#Atoms", "Total Dependence Degree",
            "Total Influential Degree", "Direct Dependence Degree",
            "Direct Influential Degree" };

    /** Adds the record.
     * 
     * @param record
     *            the record */
    public void addRecord(Record record) {
        records.add(record);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < headings.length; i++) {
            s.append(headings[i]);
            s.append(',');
        }
        s.append('\n');
        for (Record r : records) {
            s.append(r);
            s.append("\n");
        }
        return s.toString();
    }

    /** Sort.
     * 
     * @param i
     *            the i */
    public void sort(final int i) {
        Comparator<Record> comp = null;
        if (i == 0) {
            comp = new Comparator<Record>() {
                public int compare(Record o1, Record o2) {
                    return o1.entity.compareTo(o2.entity);
                }
            };
        } else {
            comp = new Comparator<Record>() {
                public int compare(Record o1, Record o2) {
                    return o2.getValue(i - 1) - o1.getValue(i - 1);
                }
            };
        }
        Collections.sort(records, comp);
    }

    /** Gets the records.
     * 
     * @return the records */
    public List<Record> getRecords() {
        return new ArrayList<Record>(records);
    }

    /** Gets the record.
     * 
     * @param rowIndex
     *            the row index
     * @return the record */
    public Record getRecord(int rowIndex) {
        return records.get(rowIndex);
    }
}
