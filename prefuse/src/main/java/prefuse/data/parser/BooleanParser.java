package prefuse.data.parser;


/**
 * DataParser instance that parses boolean values. The string "true" is
 * parsed to true values, "false" to false values. Both are case
 * insensitive.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class BooleanParser implements DataParser {
    
    /** Text string indicating a "true" value. */
    public static final String TRUE = "true";
    /** Text string indicating a "false" value. */
    public static final String FALSE = "false";
    
    /**
     * Returns boolean.class.
     *
     * @return the type
     * @see prefuse.data.parser.DataParser#getType()
     */
    public Class getType() {
        return boolean.class;
    }
    
    /**
     * Format.
     *
     * @param value the value
     * @return the string
     * @see prefuse.data.parser.DataParser#format(java.lang.Object)
     */
    public String format(Object value) {
        if ( value == null ) return null;
        if ( !(value instanceof Boolean) )
            throw new IllegalArgumentException(
              "This class can only format Objects of type Boolean.");
        return ((Boolean)value).toString();
    }
    
    /**
     * Can parse.
     *
     * @param text the text
     * @return true, if successful
     * @see prefuse.data.parser.DataParser#canParse(java.lang.String)
     */
    public boolean canParse(String text) {
        return TRUE.equalsIgnoreCase(text) || FALSE.equalsIgnoreCase(text);
    }
    
    /**
     * Parses the.
     *
     * @param text the text
     * @return the object
     * @throws DataParseException the data parse exception
     * @see prefuse.data.parser.DataParser#parse(java.lang.String)
     */
    public Object parse(String text) throws DataParseException {
        return parseBoolean(text) ? Boolean.TRUE : Boolean.FALSE;
    }
    
    /**
     * Parse a boolean value from a text string.
     *
     * @param text the text string to parse
     * @return the parsed boolean value
     * @throws DataParseException if an error occurs during parsing
     */
    public boolean parseBoolean(String text) throws DataParseException {
        if ( TRUE.equalsIgnoreCase(text) ) {
            return true;
        } else if ( FALSE.equalsIgnoreCase(text) ) {
            return false;
        } else {
            throw new DataParseException(
                "Input does not represent a boolean value: "+ text);
        }
    }
    
} // end of class BooleanParser
