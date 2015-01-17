package org.em.miron.captcha.image.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of {@link WordRenderer}.
 * A word with colored edges and adjustable stroke width.
 */
public class ColoredEdgesWordRenderer implements WordRenderer {

    private static final Random RAND = new SecureRandom();

    private static final List<Color> DEFAULT_COLORS = new ArrayList<>();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<>();
    private static final float DEFAULT_STROKE_WIDTH = 1f;
    // The text will be rendered 25%/5% of the image height/width from the X and Y axes
    private static final double YOFFSET = 0.25;
    private static final double XOFFSET = 0.05;

    private final List<Font> fonts;
    private final List<Color> colors;
    private final float strokeWidth;

    static {
        DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 40));
        DEFAULT_COLORS.add(Color.BLACK);
    }

    /**
     * Creates a {@link ColoredEdgesWordRenderer} with default color (black), font (Arial) and stroke width (1).
     */
    public ColoredEdgesWordRenderer() {
        this(DEFAULT_COLORS, DEFAULT_FONTS, DEFAULT_STROKE_WIDTH);
    }

    /**
     * Creates a {@link ColoredEdgesWordRenderer} with specified colors and fonts, and default stroke width (1).
     * @param colors list of {@link java.awt.Color}.
     * @param fonts list of {@link java.awt.Font}.
     */
    public ColoredEdgesWordRenderer(List<Color> colors, List<Font> fonts) {
        this(colors, fonts, DEFAULT_STROKE_WIDTH);
    }

    /**
     * Creates a {@link ColoredEdgesWordRenderer} with specified colors, fonts and stroke width.
     * @param colors list of {@link java.awt.Color}.
     * @param fonts list of {@link java.awt.Font}.
     * @param strokeWidth stroke width.
     */
    public ColoredEdgesWordRenderer(List<Color> colors, List<Font> fonts, float strokeWidth) {
        this.colors = colors != null ? colors : DEFAULT_COLORS;
        this.fonts = fonts != null ? fonts : DEFAULT_FONTS;
        this.strokeWidth = strokeWidth < 0 ? DEFAULT_STROKE_WIDTH : strokeWidth;
    }

    @Override
    public void render(final String word, BufferedImage image) {
        Graphics2D g = image.createGraphics();

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);

        AttributedString as = new AttributedString(word);
        as.addAttribute(TextAttribute.FONT, getRandomFont());

        FontRenderContext frc = g.getFontRenderContext();
        AttributedCharacterIterator aci = as.getIterator();

        TextLayout tl = new TextLayout(aci, frc);
        int xBaseline = (int) Math.round(image.getWidth() * XOFFSET);
        int yBaseline = image.getHeight() - (int) Math.round(image.getHeight() * YOFFSET);
        Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(xBaseline, yBaseline));

        g.setColor(getRandomColor());
        g.setStroke(new BasicStroke(strokeWidth));

        g.draw(shape);
    }

    private Color getRandomColor() {
        return (Color) getRandomObject(colors);
    }

    private Font getRandomFont() {
        return (Font) getRandomObject(fonts);
    }

    private Object getRandomObject(List<?> objs) {
        if (objs.size() == 1) {
            return objs.get(0);
        }

        int i = RAND.nextInt(objs.size());
        return objs.get(i);
    }
}
