package uk.ac.manchester.cs.demost.ui;


/**
 * The Interface Renderer.
 *
 * @param <T> the generic type
 */
public interface Renderer<T> {
	
	/**
	 * Render.
	 *
	 * @param t the t
	 * @return the string
	 */
	String render(T t);
}
