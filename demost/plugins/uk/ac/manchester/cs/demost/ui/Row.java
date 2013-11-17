package uk.ac.manchester.cs.demost.ui;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLEntity;


public class Row<T> {
	public static final char CSV_SEPARATOR='\t';
	OWLEntity entity;
	List<T> influenceValues = new ArrayList<T>();

	public void setEntity(OWLEntity entity) {
		this.entity = entity;
	}

	public void setValues(List<T> values) {
		influenceValues.clear();
		influenceValues.addAll(values);
	}

	public void addValue(T value) {
		influenceValues.add(value);
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(entity);
		for (int i = 0; i < influenceValues.size(); i++) {
			s.append(CSV_SEPARATOR);
			s.append(influenceValues.get(i));
		}
		return s.toString();
	}
	public String toString(List<?> l) {
		StringBuilder s = new StringBuilder();
		s.append(entity);
		for (int i = 0; i < influenceValues.size(); i++) {
			s.append(CSV_SEPARATOR);
			s.append(l.indexOf(influenceValues.get(i)));
		}
		return s.toString();
	}
	public String toString(Renderer<T> l) {
		StringBuilder s = new StringBuilder();
		s.append(entity);
		for (int i = 0; i < influenceValues.size(); i++) {
			s.append(CSV_SEPARATOR);
			s.append(l.render(influenceValues.get(i)));
		}
		return s.toString();
	}

	public T getValue(int i) {
		return influenceValues.get(i);
	}

	public OWLEntity getEntity() {
		return this.entity;
	}
}