package prefuse.data.io;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.parser.DataParseException;
import prefuse.data.parser.DataParser;
import prefuse.data.parser.ParserFactory;
import prefuse.util.collections.IntIterator;

/** GraphReader instance that reads in graph file formatted using the GraphML
 * file format. GraphML is an XML format supporting graph structure and typed
 * data schemas for both nodes and edges. For more information about the format,
 * please see the <a href="http://graphml.graphdrawing.org/">GraphML home
 * page</a>.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class GraphMLReader extends AbstractGraphReader implements GraphReader {
    /** Read graph.
     * 
     * @param is
     *            the is
     * @return the graph
     * @throws DataIOException
     *             the data io exception
     * @see prefuse.data.io.GraphReader#readGraph(java.io.InputStream) */
    public Graph readGraph(InputStream is) throws DataIOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphMLHandler handler = new GraphMLHandler();
            saxParser.parse(is, handler);
            return handler.getGraph();
        } catch (Exception e) {
            if (e instanceof DataIOException) {
                throw (DataIOException) e;
            } else {
                throw new DataIOException(e);
            }
        }
    }

    /** String tokens used in the GraphML format. */
    public interface Tokens {
        /** The Constant ID. */
        public static final String ID = "id";
        /** The Constant GRAPH. */
        public static final String GRAPH = "graph";
        /** The Constant EDGEDEF. */
        public static final String EDGEDEF = "edgedefault";
        /** The Constant DIRECTED. */
        public static final String DIRECTED = "directed";
        /** The Constant UNDIRECTED. */
        public static final String UNDIRECTED = "undirected";
        /** The Constant KEY. */
        public static final String KEY = "key";
        /** The Constant FOR. */
        public static final String FOR = "for";
        /** The Constant ALL. */
        public static final String ALL = "all";
        /** The Constant ATTRNAME. */
        public static final String ATTRNAME = "attr.name";
        /** The Constant ATTRTYPE. */
        public static final String ATTRTYPE = "attr.type";
        /** The Constant DEFAULT. */
        public static final String DEFAULT = "default";
        /** The Constant NODE. */
        public static final String NODE = "node";
        /** The Constant EDGE. */
        public static final String EDGE = "edge";
        /** The Constant SOURCE. */
        public static final String SOURCE = "source";
        /** The Constant TARGET. */
        public static final String TARGET = "target";
        /** The Constant DATA. */
        public static final String DATA = "data";
        /** The Constant TYPE. */
        public static final String TYPE = "type";
        /** The Constant INT. */
        public static final String INT = "int";
        /** The Constant INTEGER. */
        public static final String INTEGER = "integer";
        /** The Constant LONG. */
        public static final String LONG = "long";
        /** The Constant FLOAT. */
        public static final String FLOAT = "float";
        /** The Constant DOUBLE. */
        public static final String DOUBLE = "double";
        /** The Constant REAL. */
        public static final String REAL = "real";
        /** The Constant BOOLEAN. */
        public static final String BOOLEAN = "boolean";
        /** The Constant STRING. */
        public static final String STRING = "string";
        /** The Constant DATE. */
        public static final String DATE = "date";
    }

    /** A SAX Parser for GraphML data files. */
    public static class GraphMLHandler extends DefaultHandler implements Tokens {
        /** The m_pf. */
        protected ParserFactory m_pf = ParserFactory.getDefaultFactory();
        /** The Constant SRC. */
        protected static final String SRC = Graph.DEFAULT_SOURCE_KEY;
        /** The Constant TRG. */
        protected static final String TRG = Graph.DEFAULT_TARGET_KEY;
        /** The Constant SRCID. */
        protected static final String SRCID = SRC + '_' + ID;
        /** The Constant TRGID. */
        protected static final String TRGID = TRG + '_' + ID;
        /** The m_nsch. */
        protected Schema m_nsch = new Schema();
        /** The m_esch. */
        protected Schema m_esch = new Schema();
        /** The m_graphid. */
        protected String m_graphid;
        /** The m_graph. */
        protected Graph m_graph = null;
        /** The m_nodes. */
        protected Table m_nodes;
        /** The m_edges. */
        protected Table m_edges;
        // schema parsing
        /** The m_id. */
        protected String m_id;
        /** The m_for. */
        protected String m_for;
        /** The m_name. */
        protected String m_name;
        /** The m_type. */
        protected String m_type;
        /** The m_dflt. */
        protected String m_dflt;
        /** The m_sbuf. */
        protected StringBuffer m_sbuf = new StringBuffer();
        // node,edge,data parsing
        /** The m_key. */
        private String m_key;
        /** The m_row. */
        private int m_row = -1;
        /** The m_table. */
        private Table m_table = null;
        /** The m_node map. */
        protected HashMap m_nodeMap = new HashMap();
        /** The m_id map. */
        protected HashMap m_idMap = new HashMap();
        /** The m_directed. */
        private boolean m_directed = false;
        /** The in schema. */
        private boolean inSchema;

        public void startDocument() {
            m_nodeMap.clear();
            inSchema = true;
            m_esch.addColumn(SRC, int.class);
            m_esch.addColumn(TRG, int.class);
            m_esch.addColumn(SRCID, String.class);
            m_esch.addColumn(TRGID, String.class);
        }

        public void endDocument() throws SAXException {
            // time to actually set up the edges
            IntIterator rows = m_edges.rows();
            while (rows.hasNext()) {
                int r = rows.nextInt();
                String src = m_edges.getString(r, SRCID);
                if (!m_nodeMap.containsKey(src)) {
                    throw new SAXException("Tried to create edge with source node id="
                            + src + " which does not exist.");
                }
                int s = ((Integer) m_nodeMap.get(src)).intValue();
                m_edges.setInt(r, SRC, s);
                String trg = m_edges.getString(r, TRGID);
                if (!m_nodeMap.containsKey(trg)) {
                    throw new SAXException("Tried to create edge with target node id="
                            + trg + " which does not exist.");
                }
                int t = ((Integer) m_nodeMap.get(trg)).intValue();
                m_edges.setInt(r, TRG, t);
            }
            m_edges.removeColumn(SRCID);
            m_edges.removeColumn(TRGID);
            // now create the graph
            m_graph = new Graph(m_nodes, m_edges, m_directed);
            if (m_graphid != null) {
                m_graph.putClientProperty(ID, m_graphid);
            }
        }

        public void startElement(String namespaceURI, String localName, String qName,
                Attributes atts) {
            // first clear the character buffer
            m_sbuf.delete(0, m_sbuf.length());
            if (qName.equals(GRAPH)) {
                // parse directedness default
                String edef = atts.getValue(EDGEDEF);
                m_directed = DIRECTED.equalsIgnoreCase(edef);
                m_graphid = atts.getValue(ID);
            } else if (qName.equals(KEY)) {
                if (!inSchema) {
                    error("\"" + KEY + "\" elements can not"
                            + " occur after the first node or edge declaration.");
                }
                m_for = atts.getValue(FOR);
                m_id = atts.getValue(ID);
                m_name = atts.getValue(ATTRNAME);
                m_type = atts.getValue(ATTRTYPE);
            } else if (qName.equals(NODE)) {
                schemaCheck();
                m_row = m_nodes.addRow();
                String id = atts.getValue(ID);
                m_nodeMap.put(id, new Integer(m_row));
                m_table = m_nodes;
            } else if (qName.equals(EDGE)) {
                schemaCheck();
                m_row = m_edges.addRow();
                // do not use the id value
                // String id = atts.getValue(ID);
                // if ( id != null ) {
                // if ( !m_edges.canGetString(ID) )
                // m_edges.addColumn(ID, String.class);
                // m_edges.setString(m_row, ID, id);
                // }
                m_edges.setString(m_row, SRCID, atts.getValue(SRC));
                m_edges.setString(m_row, TRGID, atts.getValue(TRG));
                // currently only global directedness is used
                // ignore directed edge value for now
                // String dir = atts.getValue(DIRECTED);
                // boolean d = m_directed;
                // if ( dir != null ) {
                // d = dir.equalsIgnoreCase("false");
                // }
                // m_edges.setBoolean(m_row, DIRECTED, d);
                m_table = m_edges;
            } else if (qName.equals(DATA)) {
                m_key = atts.getValue(KEY);
            }
        }

        public void endElement(String namespaceURI, String localName, String qName) {
            if (qName.equals(DEFAULT)) {
                // value is in the buffer
                m_dflt = m_sbuf.toString();
            } else if (qName.equals(KEY)) {
                // time to add to the proper schema(s)
                addToSchema();
            } else if (qName.equals(DATA)) {
                // value is in the buffer
                String value = m_sbuf.toString();
                String name = (String) m_idMap.get(m_key);
                Class type = m_table.getColumnType(name);
                try {
                    Object val = parse(value, type);
                    m_table.set(m_row, name, val);
                } catch (DataParseException dpe) {
                    error(dpe);
                }
            } else if (qName.equals(NODE) || qName.equals(EDGE)) {
                m_row = -1;
                m_table = null;
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            m_sbuf.append(ch, start, length);
        }

        // --------------------------------------------------------------------
        /** Schema check. */
        protected void schemaCheck() {
            if (inSchema) {
                m_nsch.lockSchema();
                m_esch.lockSchema();
                m_nodes = m_nsch.instantiate();
                m_edges = m_esch.instantiate();
                inSchema = false;
            }
        }

        /** Adds the to schema. */
        protected void addToSchema() {
            if (m_name == null || m_name.length() == 0) {
                error("Empty " + KEY + " name.");
            }
            if (m_type == null || m_type.length() == 0) {
                error("Empty " + KEY + " type.");
            }
            try {
                Class type = parseType(m_type);
                Object dflt = m_dflt == null ? null : parse(m_dflt, type);
                if (m_for == null || m_for.equals(ALL)) {
                    m_nsch.addColumn(m_name, type, dflt);
                    m_esch.addColumn(m_name, type, dflt);
                } else if (m_for.equals(NODE)) {
                    m_nsch.addColumn(m_name, type, dflt);
                } else if (m_for.equals(EDGE)) {
                    m_esch.addColumn(m_name, type, dflt);
                } else {
                    error("Unrecognized \"" + FOR + "\" value: " + m_for);
                }
                m_idMap.put(m_id, m_name);
                m_dflt = null;
            } catch (DataParseException dpe) {
                error(dpe);
            }
        }

        /** Parses the type.
         * 
         * @param type
         *            the type
         * @return the class */
        protected Class parseType(String type) {
            type = type.toLowerCase();
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
                error("Unrecognized data type: " + type);
                return null;
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

        /** Gets the graph.
         * 
         * @return the graph */
        public Graph getGraph() {
            return m_graph;
        }

        /** Error.
         * 
         * @param s
         *            the s */
        protected void error(String s) {
            throw new RuntimeException(s);
        }

        /** Error.
         * 
         * @param e
         *            the e */
        protected void error(Exception e) {
            throw new RuntimeException(e);
        }
    } // end of inner class GraphMLHandler
} // end of class XMLGraphReader
