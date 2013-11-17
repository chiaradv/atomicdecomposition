package uk.ac.manchester.cs.demost.ui;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class RelevanceTableModel<T> {
	private final List<Row<T>> rows = new ArrayList<Row<T>>();
	String[] headings = new String[] {};

	public String[] getHeadings() {
		return headings;
	}

	public void setHeadings(String[] headings) {
		this.headings = headings;
	}

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

//	public void sort(final int i) {
//		Comparator<Record> comp = null;
//		if (i == 0) {
//			comp = new Comparator<Record>() {
//				@Override
//				public int compare(Record o1, Record o2) {
//					return o1.entity.compareTo(o2.entity);
//				}
//			};
//		} else {
//			comp = new Comparator<Record>() {
//				public int compare(Record o1, Record o2) {
//					return o2.getValue(i - 1) - o1.getValue(i - 1);
//				}
//			};
//		}
//		Collections.sort(records, comp);
//	}

	public List<Row<T>> getRecords() {
		return new ArrayList<Row<T>>(rows);
	}

	public Row<T> getRecord(int rowIndex) {
		return rows.get(rowIndex);
	}

	public int getRecordSize() {
		return rows.size();
	}
}
