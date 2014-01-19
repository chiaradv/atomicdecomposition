package prefuse.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import prefuse.data.Table;

/** Abstract base class implementation of the TableWriter interface. Provides
 * implementations for all but the
 * {@link prefuse.data.io.TableWriter#writeTable(Table, java.io.OutputStream)}
 * method.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public abstract class AbstractTableWriter implements TableWriter {
    /** Write table.
     * 
     * @param table
     *            the table
     * @param filename
     *            the filename
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.TableWriter#writeTable(prefuse.data.Table,
     *      java.lang.String) */
    @Override
    public void writeTable(Table table, String filename) throws DataIOException {
        writeTable(table, new File(filename));
    }

    /** Write table.
     * 
     * @param table
     *            the table
     * @param f
     *            the f
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.TableWriter#writeTable(prefuse.data.Table,
     *      java.io.File) */
    @Override
    public void writeTable(Table table, File f) throws DataIOException {
        try {
            writeTable(table, new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            throw new DataIOException(e);
        }
    }
} // end of abstract class AbstractTableReader
