package prefuse.data.parser;

/** DataParser instance that parses double values from a text string.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class DoubleParser implements DataParser {
    /** The m_block explicit floats. */
    private final boolean m_blockExplicitFloats = true;

    /** Returns double.class.
     * 
     * @return the type
     * @see prefuse.data.parser.DataParser#getType() */
    @Override
    public Class getType() {
        return double.class;
    }

    /** Format.
     * 
     * @param value
     *            the value
     * @return the string
     * @see prefuse.data.parser.DataParser#format(java.lang.Object) */
    @Override
    public String format(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException(
                    "This class can only format Objects of type Number.");
        }
        return String.valueOf(((Number) value).doubleValue());
    }

    /** Can parse.
     * 
     * @param text
     *            the text
     * @return true, if successful
     * @see prefuse.data.parser.DataParser#canParse(java.lang.String) */
    @Override
    public boolean canParse(String text) {
        try {
            if (m_blockExplicitFloats && text.endsWith("f")) {
                // don't try to convert floats
                return false;
            }
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Parses the.
     * 
     * @param text
     *            the text
     * @return the object
     * @throws DataParseException
     *             the data parse exception
     * @see prefuse.data.parser.DataParser#parse(java.lang.String) */
    @Override
    public Object parse(String text) throws DataParseException {
        return new Double(parseDouble(text));
    }

    /** Parse a double value from a text string.
     * 
     * @param text
     *            the text string to parse
     * @return the parsed double value
     * @throws DataParseException
     *             if an error occurs during parsing */
    public static double parseDouble(String text) throws DataParseException {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new DataParseException(e);
        }
    }
} // end of class DoubleParser
