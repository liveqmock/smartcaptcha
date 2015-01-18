package org.em.miron.captcha.image.background;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Creates a gradiated background with the given <i>from</i> and <i>to</i>
 * Color values. If none are specified they default to light gray and white
 * respectively.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public class GradiatedBackground extends AbstractBackground {

    private final Color fromColor;
    private final Color toColor;

    /**
     * Creates a gradiated background with defaults colors: from dark gray to white.
     */
    public GradiatedBackground() {
        this(Color.DARK_GRAY, Color.WHITE);
    }

    /**
     * Creates a gradiated background with specified <i>from</i> and <i>to</i> colors.
     * @param from the instance of {@link java.awt.Color}.
     * @param to the instance of {@link java.awt.Color}.
     */
    public GradiatedBackground(Color from, Color to) {
        fromColor = from;
        toColor = to;
    }

    @Override
    public BufferedImage getBackground(int width, int height) {
        // create an opaque image
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHints(hints);

        // create the gradient color
        GradientPaint ytow = new GradientPaint(0, 0, fromColor, width, height, toColor);

        g.setPaint(ytow);
        // draw gradient color
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        // draw the transparent image over the background
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return img;
    }
}
