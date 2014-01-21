package prefuse.data.io;

import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tree;
import prefuse.data.parser.DataParseException;
import prefuse.data.parser.DataParser;
import prefuse.data.parser.ParserFactory;

/** GraphReader instance that reads in tree-structured data in the XML-based
 * TreeML format. TreeML is an XML format originally created for the 2003
 * InfoVis conference contest. A DTD (Document Type Definition) for TreeML is <a
 * href="http://www.nomencurator.org/InfoVis2003/download/treeml.dtd"> available
 * online</a>.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class TreeMLReader extends AbstractGraphReader {
    /** The m_pf. */
    private final ParserFactory m_pf = ParserFactory.getDefaultFactory();

    /** Read graph.
     * 
     * @param is
     *            the is
     * @return the graph
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.GraphReader#readGraph(java.io.InputStream) */
    @Override
    public Graph readGraph(InputStream is) throws DataIOException {
        try {
            TreeMLHandler handler = new TreeMLHandler();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
            return handler.getTree();
        } catch (Exception e) {
            throw new DataIOException(e);
        }
    }

    /** String tokens used in the TreeML format. */
    public static interface Tokens {
        /** The Constant TREE. */
        public static final String TREE = "tree";
        /** The Constant BRANCH. */
        public static final String BRANCH = "branch";
        /** The Constant LEAF. */
        public static final String LEAF = "leaf";
        /** The Constant ATTR. */
        public static final String ATTR = "attribute";
        /** The Constant NAME. */
        public static final String NAME = "name";
        /** The Constant VALUE. */
        public static final String VALUE = "value";
        /** The Constant TYPE. */
        public static final String TYPE = "type";
        /** The Constant DECLS. */
        public static final String DECLS = "declarations";
        /** The Constant DECL. */
        public static final String DECL = "attributeDecl";
        /** The Constant INT. */
        public static final String INT = "Int";
        /** The Constant INTEGER. */
        public static final String INTEGER = "Integer";
        /** The Constant LONG. */
        public static final String LONG = "Long";
        /** The Constant FLOAT. */
        public static final String FLOAT = "Float";
        /** The Constant REAL. */
        public static final String REAL = "Real";
        /** The Constant STRING. */
        public static final String STRING = "String";
        /** The Constant DATE. */
        public static final String DATE = "Date";
        /** The Constant CATEGORY. */
        public static final String CATEGORY = "Category";
        // prefuse-specific allowed types
        /** The Constant BOOLEAN. */
        public static final String BOOLEAN = "Boolean";
        /** The Constant DOUBLE. */
        public static final String DOUBLE = "Double";
    }

    /** A SAX Parser for TreeML data files. */
    public class TreeMLHandler extends DefaultHandler implements Tokens {
        /** The m_nodes. */
        private Table m_nodes = null;
        /** The m_tree. */
        private Tree m_tree = null;
        /** The m_active node. */
        private Node m_activeNode = null;
        /** The m_in schema. */
        private boolean m_inSchema = true;

        @Override
        public void startDocument() {
            m_tree = new Tree();
            m_nodes = m_tree.getNodeTable();
        }

        /** Schema check. */
        private void schemaCheck() {
            if (m_inSchema) {
                m_inSchema = false;
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) {
            if (qName.equals(BRANCH) || qName.equals(LEAF)) {
                m_activeNode = m_activeNode.getParent();
            }
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName,
                Attributes atts) {
            if (qName.equals(DECL)) {
                if (!m_inSchema) {
                    throw new RuntimeException("All declarations must be done "
                            + "before nodes begin");
                }
                String name = atts.getValue(NAME);
                String type = atts.getValue(TYPE);
                Class t = parseType(type);
                m_nodes.addColumn(name, t);
            } else if (qName.equals(BRANCH) || qName.equals(LEAF)) {
                schemaCheck();
                // parse a node element
                Node n;
                if (m_activeNode == null) {
                    n = m_tree.addRoot();
                } else {
                    n = m_tree.addChild(m_activeNode);
                }
                m_activeNode = n;
            } else if (qName.equals(ATTR)) {
                // parse an attribute
                parseAttribute(atts);
            }
        }

        /** Parses the attribute.
         * 
         * @param atts
         *            the atts */
        protected void parseAttribute(Attributes atts) {
            String alName, name = null, value = null;
            for (int i = 0; i < atts.getLength(); i++) {
                alName = atts.getQName(i);
                if (alName.equals(NAME)) {
                    name = atts.getValue(i);
                } else if (alName.equals(VALUE)) {
                    value = atts.getValue(i);
                }
            }
            if (name == null || value == null) {
                System.err.println("Attribute under-specified");
                return;
            }
            try {
                Object val = parse(value, m_nodes.getColumnType(name));
                m_activeNode.set(name, val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /** Parses the.
         * 
         * @param s
         *            the s
         * @param type
         *            the type
         * @return the object
         * @throws DataParseException
         *             the data parse exception */
        protected Object parse(String s, Class type) throws DataParseException {
            DataParser dp = m_pf.getParser(type);
            return dp.parse(s);
        }

        /** Parses the type.
         * 
         * @param type
         *            the type
         * @return the class */
        protected Class parseType(String type) {
            type = Character.toUpperCase(type.charAt(0))
                    + type.substring(1).toLowerCase();
            if (type.equals(INT) || type.equals(INTEGER)) {
                return int.class;
            } else if (type.equals(LONG)) {
                return long.class;
            } else if (type.equals(FLOAT)) {
                return float.class;
            } else if (type.equals(DOUBLE) || type.equals(REAL)) {
                return double.class;
            } else if (type.equals(BOOLEAN)) {
                return boolean.class;
            } else if (type.equals(STRING)) {
                return String.class;
            } else if (type.equals(DATE)) {
                return Date.class;
            } else {
                throw new RuntimeException("Unrecognized data type: " + type);
            }
        }

        /** Gets the tree.
         * 
         * @return the tree */
        public Tree getTree() {
            return m_tree;
        }
    } // end of inner class TreeMLHandler
} // end of class TreeMLTReeReader
