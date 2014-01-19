package prefuse.data.io;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;

/** TableWriter that writes out a text table in the comma-separated-values
 * format. By default, a header row containing the column names is included in
 * the output.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class CSVTableWriter extends AbstractTableWriter {
    /** The m_print header. */
    private boolean m_printHeader;

    /** Create a new CSVTableWriter that writes comma separated values files. */
    public CSVTableWriter() {
        this(true);
    }

    /** Create a new CSVTableWriter.
     * 
     * @param printHeader
     *            indicates if a header row should be printed */
    public CSVTableWriter(boolean printHeader) {
        m_printHeader = printHeader;
    }

    // ------------------------------------------------------------------------
    /** Indicates if this writer will write a header row with the column names.
     * 
     * @return true if a header row will be printed, false otherwise */
    public boolean isPrintHeader() {
        return m_printHeader;
    }

    /** Sets if this writer will write a header row with the column names.
     * 
     * @param printHeader
     *            true to print a header row, false otherwise */
    public void setPrintHeader(boolean printHeader) {
        m_printHeader = printHeader;
    }

    // ------------------------------------------------------------------------
    /** Write table.
     * 
     * @param table
     *            the table
     * @param os
     *            the os
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.TableWriter#writeTable(prefuse.data.Table,
     *      java.io.OutputStream) */
    @Override
    public void writeTable(Table table, OutputStream os) throws DataIOException {
        try {
            // get print stream
            PrintStream out = new PrintStream(new BufferedOutputStream(os));
            // write out header row
            if (m_printHeader) {
                for (int i = 0; i < table.getColumnCount(); ++i) {
                    if (i > 0) {
                        out.print(',');
                    }
                    out.print(makeCSVSafe(table.getColumnName(i)));
                }
                out.println();
            }
            // write out data
            for (IntIterator rows = table.rows(); rows.hasNext();) {
                int row = rows.nextInt();
                for (int i = 0; i < table.getColumnCount(); ++i) {
                    if (i > 0) {
                        out.print(',');
                    }
                    String str = table.getString(row, table.getColumnName(i));
                    out.print(makeCSVSafe(str));
                }
                out.println();
            }
            // finish up
            out.flush();
        } catch (Exception e) {
            throw new DataIOException(e);
        }
    }

    /** Make csv safe.
     * 
     * @param s
     *            the s
     * @return the string */
    private String makeCSVSafe(String s) {
        int q = -1;
        if ((q = s.indexOf('\"')) >= 0 || s.indexOf(',') >= 0 || s.indexOf('\n') >= 0
                || Character.isWhitespace(s.charAt(0))
                || Character.isWhitespace(s.charAt(s.length() - 1))) {
            if (q >= 0) {
                s = s.replaceAll("\"", "\"\"");
            }
            s = "\"" + s + "\"";
        }
        return s;
    }
} // end of class CSVTableWriter
