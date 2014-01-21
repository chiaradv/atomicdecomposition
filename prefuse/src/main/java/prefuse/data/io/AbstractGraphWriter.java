package prefuse.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import prefuse.data.Graph;

/** Abstract base class implementation of the GraphWriter interface. Provides
 * implementations for all but the
 * {@link prefuse.data.io.GraphWriter#writeGraph(Graph, OutputStream)} method.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractGraphWriter implements GraphWriter {
    /** Write graph.
     * 
     * @param graph
     *            the graph
     * @param filename
     *            the filename
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.GraphWriter#writeGraph(prefuse.data.Graph,
     *      java.lang.String) */
    @Override
    public void writeGraph(Graph graph, String filename) throws DataIOException {
        writeGraph(graph, new File(filename));
    }

    /** Write graph.
     * 
     * @param graph
     *            the graph
     * @param f
     *            the f
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.GraphWriter#writeGraph(prefuse.data.Graph,
     *      java.io.File) */
    @Override
    public void writeGraph(Graph graph, File f) throws DataIOException {
        try {
            writeGraph(graph, new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            throw new DataIOException(e);
        }
    }
} // end of abstract class AbstractGraphReader
