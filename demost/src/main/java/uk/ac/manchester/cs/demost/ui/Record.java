package uk.ac.manchester.cs.demost.ui;

import org.semanticweb.owlapi.model.OWLEntity;

/** The Class Record. */
public class Record {
    /** The entity. */
    OWLEntity entity;
    /** The influence values. */
    int[] influenceValues = new int[5];

    /** Sets the entity.
     * 
     * @param entity
     *            the new entity */
    public void setEntity(OWLEntity entity) {
        this.entity = entity;
    }

    /** Sets the values.
     * 
     * @param values
     *            the new values */
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

    /** Gets the value.
     * 
     * @param i
     *            the i
     * @return the value */
    public int getValue(int i) {
        return influenceValues[i];
    }
}
