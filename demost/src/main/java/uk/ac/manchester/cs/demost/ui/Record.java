package uk.ac.manchester.cs.demost.ui;

import org.semanticweb.owlapi.model.OWLEntity;

public class Record {
	OWLEntity entity;
	int[] influenceValues = new int[5];

	public void setEntity(OWLEntity entity) {
		this.entity = entity;
	}

	public void setValues(int[] values) {
		for (int i = 0; i < values.length; i++) {
			influenceValues[i] = values[i];
		}
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(entity);
		for (int i = 0; i < influenceValues.length; i++) {
			s.append(",");
			s.append(influenceValues[i]);
		}
		return s.toString();
	}

	public int getValue(int i) {
		return influenceValues[i];
	}
}