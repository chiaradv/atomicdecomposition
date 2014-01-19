package uk.ac.manchester.cs.demost.ui;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLEntity;

/** The Class Row.
 * 
 * @param <T>
 *            the generic type */
public class Row<T> {
    /** The Constant CSV_SEPARATOR. */
    public static final char CSV_SEPARATOR = '\t';
    /** The entity. */
    OWLEntity entity;
    /** The influence values. */
    List<T> influenceValues = new ArrayList<T>();

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
    public void setValues(List<T> values) {
        influenceValues.clear();
        influenceValues.addAll(values);
    }

    /** Adds the value.
     * 
     * @param value
     *            the value */
    public void addValue(T value) {
        influenceValues.add(value);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(entity);
        for (int i = 0; i < influenceValues.size(); i++) {
            s.append(CSV_SEPARATOR);
            s.append(influenceValues.get(i));
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
        s.append(entity);
        for (int i = 0; i < influenceValues.size(); i++) {
            s.append(CSV_SEPARATOR);
            s.append(l.indexOf(influenceValues.get(i)));
        }
        return s.toString();
    }

    /** To string.
     * 
     * @param l
     *            the l
     * @return the string */
    public String toString(Renderer<T> l) {
        StringBuilder s = new StringBuilder();
        s.append(entity);
        for (int i = 0; i < influenceValues.size(); i++) {
            s.append(CSV_SEPARATOR);
            s.append(l.render(influenceValues.get(i)));
        }
        return s.toString();
    }

    /** Gets the value.
     * 
     * @param i
     *            the i
     * @return the value */
    public T getValue(int i) {
        return influenceValues.get(i);
    }

    /** Gets the entity.
     * 
     * @return the entity */
    public OWLEntity getEntity() {
        return this.entity;
    }
}
