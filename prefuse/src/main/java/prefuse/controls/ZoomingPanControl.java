package prefuse.controls;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import prefuse.Display;
import prefuse.activity.Activity;
import prefuse.activity.SlowInSlowOutPacer;

/** <p>
 * Allows users to pan over a display such that the display zooms in and out
 * proportionally to how fast the pan is performed.
 * </p>
 * <p>
 * The algorithm used is that of Takeo Igarishi and Ken Hinckley in their
 * research paper <a
 * href="http://citeseer.ist.psu.edu/igarashi00speeddependent.html">
 * Speed-dependent Automatic Zooming for Browsing Large Documents</a>, UIST
 * 2000.
 * </p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a> */
public class ZoomingPanControl extends ControlAdapter {
    /** The started. */
    private boolean repaint = true, started = false;
    /** The mouse up. */
    private Point mouseDown, mouseCur, mouseUp;
    /** The dy. */
    private int dx, dy;
    /** The d. */
    private double d = 0;
    /** The s0. */
    private final double v0 = 75.0, d0 = 50, d1 = 400, s0 = .1;
    /** The update. */
    private final UpdateActivity update = new UpdateActivity();
    /** The finish. */
    private final FinishActivity finish = new FinishActivity();

    /** Create a new ZoomingPanControl. */
    public ZoomingPanControl() {
        this(true);
    }

    /** Create a new ZoomingPanControl.
     * 
     * @param repaint
     *            true if repaint requests should be issued while panning and
     *            zooming. false if repaint requests will come from elsewhere
     *            (e.g., a continuously running action). */
    public ZoomingPanControl(boolean repaint) {
        this.repaint = repaint;
    }

    /** Mouse pressed.
     * 
     * @param e
     *            the e
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent) */
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Display display = (Display) e.getComponent();
            display.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            mouseDown = e.getPoint();
        }
    }

    /** Mouse dragged.
     * 
     * @param e
     *            the e
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent) */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseCur = e.getPoint();
            dx = mouseCur.x - mouseDown.x;
            dy = mouseCur.y - mouseDown.y;
            d = Math.sqrt(dx * dx + dy * dy);
            if (!started) {
                Display display = (Display) e.getComponent();
                update.setDisplay(display);
                update.run();
            }
        }
    }

    /** Mouse released.
     * 
     * @param e
     *            the e
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent) */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            update.cancel();
            started = false;
            Display display = (Display) e.getComponent();
            mouseUp = e.getPoint();
            finish.setDisplay(display);
            finish.run();
            display.setCursor(Cursor.getDefaultCursor());
        }
    }

    /** The Class UpdateActivity. */
    private class UpdateActivity extends Activity {
        /** The display. */
        private Display display;
        /** The last time. */
        private long lastTime = 0;

        /** Instantiates a new update activity. */
        public UpdateActivity() {
            super(-1, 15, 0);
        }

        /** Sets the display.
         * 
         * @param display
         *            the new display */
        public void setDisplay(Display display) {
            this.display = display;
        }

        @Override
        protected void run(long elapsedTime) {
            double sx = display.getTransform().getScaleX();
            double s, v;
            if (d <= d0) {
                s = 1.0;
                v = v0 * (d / d0);
            } else {
                s = d >= d1 ? s0 : Math.pow(s0, (d - d0) / (d1 - d0));
                v = v0;
            }
            s = s / sx;
            double dd = v * (elapsedTime - lastTime) / 1000;
            lastTime = elapsedTime;
            double deltaX = -dd * dx / d;
            double deltaY = -dd * dy / d;
            display.pan(deltaX, deltaY);
            if (s != 1.0) {
                display.zoom(mouseCur, s);
            }
            if (repaint) {
                display.repaint();
            }
        }
    } // end of class UpdateActivity

    /** The Class FinishActivity. */
    private class FinishActivity extends Activity {
        /** The display. */
        private Display display;
        /** The scale. */
        private double scale;

        /** Instantiates a new finish activity. */
        public FinishActivity() {
            super(1500, 15, 0);
            setPacingFunction(new SlowInSlowOutPacer());
        }

        /** Sets the display.
         * 
         * @param display
         *            the new display */
        public void setDisplay(Display display) {
            this.display = display;
            scale = display.getTransform().getScaleX();
            double z = scale < 1.0 ? 1 / scale : scale;
            setDuration((long) (500 + 500 * Math.log(1 + z)));
        }

        @Override
        protected void run(long elapsedTime) {
            double f = getPace(elapsedTime);
            double s = display.getTransform().getScaleX();
            double z = (f + (1 - f) * scale) / s;
            display.zoom(mouseUp, z);
            if (repaint) {
                display.repaint();
            }
        }
    } // end of class FinishActivity
} // end of class ZoomingPanControl
