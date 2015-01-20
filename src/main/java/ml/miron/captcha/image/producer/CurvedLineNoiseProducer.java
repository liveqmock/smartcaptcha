package ml.miron.captcha.image.producer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Adds a randomly curved line to the image.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public class CurvedLineNoiseProducer implements NoiseProducer {

    private static final Random RAND = new SecureRandom();

    private final Color color;
    private final float width;

    /**
     * Creates a {@link CurvedLineNoiseProducer} with default color and width.
     */
    public CurvedLineNoiseProducer() {
        this(Color.BLACK, 3.0f);
    }

    /**
     * Creates a {@link CurvedLineNoiseProducer} with specified color and width.
     * @param color color of line.
     * @param width width of line.
     */
    public CurvedLineNoiseProducer(Color color, float width) {
        this.color = color;
        this.width = width;
    }

    @Override
    public void makeNoise(BufferedImage image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // the curve from where the points are taken
        CubicCurve2D cc = new CubicCurve2D.Float(imageWidth * .1f, imageHeight
                * RAND.nextFloat(), imageWidth * .1f, imageHeight
                * RAND.nextFloat(), imageWidth * .25f, imageHeight
                * RAND.nextFloat(), imageWidth * .9f, imageHeight
                * RAND.nextFloat());

        // creates an iterator to define the boundary of the flattened curve
        PathIterator pi = cc.getPathIterator(null, 2);
        Point2D[] tmp = new Point2D[200];
        int i = 0;

        // while pi is iterating the curve, adds points to tmp array
        while (!pi.isDone()) {
            float[] coords = new float[6];
            int i1 = pi.currentSegment(coords);
            if (i1 == PathIterator.SEG_MOVETO || i1 == PathIterator.SEG_LINETO) {
                tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pi.next();
        }

        // the points where the line changes the stroke and direction
        Point2D[] pts = new Point2D[i];
        // copies points from tmp to pts
        System.arraycopy(tmp, 0, pts, 0, i);

        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        graph.setColor(color);

        // for the maximum 3 point change the stroke and direction
        for (i = 0; i < pts.length - 1; i++) {
            if (i < 3) {
                graph.setStroke(new BasicStroke(this.width));
            }
            graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(),
                    (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
        }

        graph.dispose();
    }
}
