package prefuse.controls;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import prefuse.visual.VisualItem;



/**
 * Adapter class for processing prefuse interface events. Subclasses can
 * override the desired methods to perform user interface event handling.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ControlAdapter implements Control {

    /** The m_enabled. */
    private boolean m_enabled = true;
    
    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     * @see prefuse.controls.Control#isEnabled()
     */
    public boolean isEnabled() {
        return m_enabled;
    }
    
    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     * @see prefuse.controls.Control#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        m_enabled = enabled;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Item dragged.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item moved.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemMoved(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemMoved(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item wheel moved.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemWheelMoved(prefuse.visual.VisualItem, java.awt.event.MouseWheelEvent)
     */
    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
    } 

    /**
     * Item clicked.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemClicked(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemClicked(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item pressed.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item released.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item entered.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item exited.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
    } 

    /**
     * Item key pressed.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemKeyPressed(prefuse.visual.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyPressed(VisualItem item, KeyEvent e) {
    } 

    /**
     * Item key released.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemKeyReleased(prefuse.visual.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyReleased(VisualItem item, KeyEvent e) {
    } 

    /**
     * Item key typed.
     *
     * @param item the item
     * @param e the e
     * @see prefuse.controls.Control#itemKeyTyped(prefuse.visual.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyTyped(VisualItem item, KeyEvent e) {
    } 

    /**
     * Mouse entered.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {    
    } 

    /**
     * Mouse exited.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {     
    } 

    /**
     * Mouse pressed.
     *
     * @param e the e
     * @see prefuse.controls.Control#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {    
    } 

    /**
     * Mouse released.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    } 

    /**
     * Mouse clicked.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
    } 

    /**
     * Mouse dragged.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
    } 

    /**
     * Mouse moved.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
    } 

    /**
     * Mouse wheel moved.
     *
     * @param e the e
     * @see prefuse.controls.Control#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
    } 

    /**
     * Key pressed.
     *
     * @param e the e
     * @see prefuse.controls.Control#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
    } 

    /**
     * Key released.
     *
     * @param e the e
     * @see prefuse.controls.Control#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
    } 

    /**
     * Key typed.
     *
     * @param e the e
     * @see prefuse.controls.Control#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
    } 

} // end of class ControlAdapter
