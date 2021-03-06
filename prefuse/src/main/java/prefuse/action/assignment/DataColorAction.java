package prefuse.action.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import prefuse.Constants;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.util.DataLib;
import prefuse.util.MathLib;
import prefuse.visual.VisualItem;

/** <p>
 * Assignment Action that assigns color values for a group of items based upon a
 * data field. The type of color encoding used is dependent upon the reported
 * data type. Nominal (categorical) data is encoded using a different hue for
 * each unique data value. Ordinal (ordered) and Numerical (quantitative) data
 * is shown using a grayscale color ramp. In all cases, the default color
 * palette used by this Action can be replaced with a client-specified palette
 * provided to the DataColorAction constructor.
 * </p>
 * <p>
 * The color spectra for numerical data is continuous by default, but can also
 * be binned into a few discrete steps (see {@link #setBinCount(int)}).
 * Quantitative data can also be colored on different numerical scales. The
 * default scale is a linear scale (specified by {@link Constants#LINEAR_SCALE}
 * ), but logarithmic and square root scales can be used (specified by
 * {@link Constants#LOG_SCALE} and {@link Constants#SQRT_SCALE} respectively.
 * Finally, the scale can be broken into quantiles, reflecting the statistical
 * distribution of the values rather than just the total data value range, using
 * the {@link Constants#QUANTILE_SCALE} value. For the quantile scale to work,
 * you also need to specify the number of bins to use (see
 * {@link #setBinCount(int)}). This value will determine the number of quantiles
 * that the data should be divided into.
 * </p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class DataColorAction extends ColorAction {
    /** The m_data field. */
    private String m_dataField;
    /** The m_type. */
    private int m_type;
    /** The m_scale. */
    private int m_scale = Constants.LINEAR_SCALE;
    /** The m_temp scale. */
    private int m_tempScale;
    /** The m_dist. */
    private double[] m_dist;
    /** The m_bins. */
    private int m_bins = Constants.CONTINUOUS;
    /** The m_omap. */
    private Map m_omap;
    /** The m_olist. */
    private Object[] m_olist;
    /** The m_cmap. */
    private final ColorMap m_cmap = new ColorMap(null, 0, 1);
    /** The m_palette. */
    private int[] m_palette;

    /** Create a new DataColorAction.
     * 
     * @param group
     *            the data group to process
     * @param dataField
     *            the data field to base size assignments on
     * @param dataType
     *            the data type to use for the data field. One of
     * @param colorField
     *            the color field to assign {@link prefuse.Constants#NOMINAL},
     *            {@link prefuse.Constants#ORDINAL}, or
     *            {@link prefuse.Constants#NUMERICAL}, for whether the data
     *            field represents categories, an ordered sequence, or numerical
     *            values. */
    public DataColorAction(String group, String dataField, int dataType, String colorField) {
        super(group, colorField);
        setDataType(dataType);
        setDataField(dataField);
    }

    /** Create a new DataColorAction.
     * 
     * @param group
     *            the data group to process
     * @param dataField
     *            the data field to base size assignments on
     * @param dataType
     *            the data type to use for the data field. One of
     * @param colorField
     *            the color field to assign
     * @param palette
     *            the color palette to use. See
     *            {@link prefuse.Constants#NOMINAL},
     *            {@link prefuse.Constants#ORDINAL}, or
     *            {@link prefuse.Constants#NUMERICAL}, for whether the data
     *            field represents categories, an ordered sequence, or numerical
     *            values. {@link prefuse.util.ColorLib} for color palette
     *            generators. */
    public DataColorAction(String group, String dataField, int dataType,
            String colorField, int[] palette) {
        super(group, colorField);
        setDataType(dataType);
        setDataField(dataField);
        m_palette = palette;
    }

    // ------------------------------------------------------------------------
    /** Returns the data field used to encode size values.
     * 
     * @return the data field that is mapped to size values */
    public String getDataField() {
        return m_dataField;
    }

    /** Set the data field used to encode size values.
     * 
     * @param field
     *            the data field to map to size values */
    public void setDataField(String field) {
        m_dataField = field;
    }

    /** Return the data type used by this action. This value is one of
     * 
     * @return the data type used by this action
     *         {@link prefuse.Constants#NOMINAL},
     *         {@link prefuse.Constants#ORDINAL}, or
     *         {@link prefuse.Constants#NUMERICAL}. */
    public int getDataType() {
        return m_type;
    }

    /** Set the data type used by this action.
     * 
     * @param type
     *            the data type used by this action, one of
     *            {@link prefuse.Constants#NOMINAL},
     *            {@link prefuse.Constants#ORDINAL}, or
     *            {@link prefuse.Constants#NUMERICAL}. */
    public void setDataType(int type) {
        if (type < 0 || type >= Constants.DATATYPE_COUNT) {
            throw new IllegalArgumentException("Unrecognized data type: " + type);
        }
        m_type = type;
    }

    /** Returns the scale type used for encoding color values from the data. This
     * value is only used for {@link prefuse.Constants#NUMERICAL} data.
     * 
     * @return the scale type. One of {@link prefuse.Constants#LINEAR_SCALE},
     *         {@link prefuse.Constants#LOG_SCALE},
     *         {@link prefuse.Constants#SQRT_SCALE},
     *         {@link prefuse.Constants#QUANTILE_SCALE}. */
    public int getScale() {
        return m_scale;
    }

    /** Set the scale (linear, square root, or log) to use for encoding color
     * values from the data. This value is only used for
     * 
     * @param scale
     *            the scale type to use. This value should be one of
     *            {@link prefuse.Constants#NUMERICAL} data.
     *            {@link prefuse.Constants#LINEAR_SCALE},
     *            {@link prefuse.Constants#SQRT_SCALE},
     *            {@link prefuse.Constants#LOG_SCALE},
     *            {@link prefuse.Constants#QUANTILE_SCALE}. If
     *            {@link prefuse.Constants#QUANTILE_SCALE} is used, the number
     *            of bins to use must also be specified to a value greater than
     *            zero using the {@link #setBinCount(int)} method. */
    public void setScale(int scale) {
        if (scale < 0 || scale >= Constants.SCALE_COUNT) {
            throw new IllegalArgumentException("Unrecognized scale value: " + scale);
        }
        m_scale = scale;
    }

    /** Returns the number of "bins" or discrete steps of color. This value is
     * only used for numerical data.
     * 
     * @return the number of bins. */
    public int getBinCount() {
        return m_bins;
    }

    /** Sets the number of "bins" or or discrete steps of color. This value is
     * only used for numerical data.
     * 
     * @param count
     *            the number of bins to set. The value
     *            {@link Constants#CONTINUOUS} indicates not to use any binning.
     *            If the scale type set using the {@link #setScale(int)} method
     *            is {@link Constants#QUANTILE_SCALE}, the bin count
     *            <strong>must</strong> be greater than zero. */
    public void setBinCount(int count) {
        if (m_scale == Constants.QUANTILE_SCALE && count <= 0) {
            throw new IllegalArgumentException(
                    "The quantile scale can not be used without binning. "
                            + "Use a bin value greater than zero.");
        }
        m_bins = count;
    }

    /** This operation is not supported by the DataColorAction type. Calling this
     * method will result in a thrown exception.
     * 
     * @param color
     *            the new default color
     * @see prefuse.action.assignment.ColorAction#setDefaultColor(int) */
    @Override
    public void setDefaultColor(int color) {
        throw new UnsupportedOperationException();
    }

    /** Manually sets the ordered list of values to use for color assignment.
     * Normally, this ordering is computed using the methods of the
     * 
     * @param values
     *            the ordered list of values. If this array is missing a value
     *            contained within data processed by this action, an exception
     *            will be thrown when this action is run.
     *            {@link prefuse.util.DataLib} class. This method allows you to
     *            set your own custom ordering. This ordering corresponds to the
     *            ordering of colors in this action's color palette. If the
     *            provided array of values is missing a value contained within
     *            the data, an exception will result during execution of this
     *            action. */
    public void setOrdinalMap(Object[] values) {
        m_olist = values;
        m_omap = new HashMap();
        for (int i = 0; i < values.length; ++i) {
            m_omap.put(values[i], new Integer(i));
        }
    }

    // ------------------------------------------------------------------------
    /** Set up the state of this encoding Action.
     * 
     * @see prefuse.action.EncoderAction#setup() */
    @Override
    protected void setup() {
        int size = 64;
        int[] palette = m_palette;
        // switch up scale if necessary
        m_tempScale = m_scale;
        if (m_scale == Constants.QUANTILE_SCALE && m_bins <= 0) {
            Logger.getLogger(getClass().getName()).warning(
                    "Can't use quantile scale with no binning. "
                            + "Defaulting to linear scale. Set the bin value "
                            + "greater than zero to use a quantile scale.");
            m_scale = Constants.LINEAR_SCALE;
        }
        // compute distribution and color map
        switch (m_type) {
            case Constants.NOMINAL:
            case Constants.ORDINAL:
                m_dist = getDistribution();
                size = m_omap.size();
                palette = m_palette != null ? m_palette : createPalette(size);
                m_cmap.setColorPalette(palette);
                m_cmap.setMinValue(m_dist[0]);
                m_cmap.setMaxValue(m_dist[1]);
                return;
            case Constants.NUMERICAL:
                m_dist = getDistribution();
                size = m_bins > 0 ? m_bins : size;
                palette = m_palette != null ? m_palette : createPalette(size);
                m_cmap.setColorPalette(palette);
                m_cmap.setMinValue(0.0);
                m_cmap.setMaxValue(1.0);
                return;
        }
    }

    @Override
    protected void finish() {
        // reset scale in case it needed to be changed due to errors
        m_scale = m_tempScale;
    }

    /** Computes the distribution (either min/max or quantile values) used to
     * help assign colors to data values.
     * 
     * @return the distribution */
    protected double[] getDistribution() {
        TupleSet ts = m_vis.getGroup(m_group);
        if (m_type == Constants.NUMERICAL) {
            m_omap = null;
            if (m_scale == Constants.QUANTILE_SCALE && m_bins > 0) {
                double[] values = DataLib.toDoubleArray(ts.tuples(), m_dataField);
                return MathLib.quantiles(m_bins, values);
            } else {
                double[] dist = new double[2];
                dist[0] = DataLib.min(ts, m_dataField).getDouble(m_dataField);
                dist[1] = DataLib.max(ts, m_dataField).getDouble(m_dataField);
                return dist;
            }
        } else {
            if (m_olist == null) {
                m_omap = DataLib.ordinalMap(ts, m_dataField);
            }
            return new double[] { 0, m_omap.size() - 1 };
        }
    }

    /** Create a color palette of the requested type and size.
     * 
     * @param size
     *            the size
     * @return the int[] */
    protected int[] createPalette(int size) {
        switch (m_type) {
            case Constants.NOMINAL:
                return ColorLib.getCategoryPalette(size);
            case Constants.NUMERICAL:
            case Constants.ORDINAL:
            default:
                return ColorLib.getGrayscalePalette(size);
        }
    }

    /** Gets the color.
     * 
     * @param item
     *            the item
     * @return the color
     * @see prefuse.action.assignment.ColorAction#getColor(prefuse.visual.VisualItem) */
    @Override
    public int getColor(VisualItem item) {
        // check for any cascaded rules first
        Object o = lookup(item);
        if (o != null) {
            if (o instanceof ColorAction) {
                return ((ColorAction) o).getColor(item);
            } else if (o instanceof Integer) {
                return ((Integer) o).intValue();
            } else {
                Logger.getLogger(this.getClass().getName()).warning(
                        "Unrecognized Object from predicate chain.");
            }
        }
        // otherwise perform data-driven assignment
        switch (m_type) {
            case Constants.NUMERICAL:
                double v = item.getDouble(m_dataField);
                double f = MathLib.interp(m_scale, v, m_dist);
                return m_cmap.getColor(f);
            default:
                Integer idx = (Integer) m_omap.get(item.get(m_dataField));
                return m_cmap.getColor(idx.doubleValue());
        }
    }
} // end of class DataColorAction
