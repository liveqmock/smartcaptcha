package org.em.miron.captcha.image.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Default implementation of {@link WordRenderer}.
 */
public class DefaultWordRenderer implements WordRenderer {

    private static final Random RAND = new SecureRandom();
    private static final List<Color> DEFAULT_COLORS = new ArrayList<>();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<>();
    // The text will be rendered 25%/5% of the image height/width from the X and Y axes
    private static final double YOFFSET = 0.25;
    private static final double XOFFSET = 0.05;

    static {
        DEFAULT_COLORS.add(Color.BLACK);
        DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 40));
        DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 40));
    }

    private final List<Color> colors = new ArrayList<>();
    private final List<Font> fonts = new ArrayList<>();

    /**
     * Creates a {@link DefaultWordRenderer} with default color (black) and fonts (Arial and Courier).
     */
    public DefaultWordRenderer() {
        this(DEFAULT_COLORS, DEFAULT_FONTS);
    }

    /**
     * Creates a {@link DefaultWordRenderer} with specified colors and fonts.
     * @param colors list of {@link java.awt.Color}.
     * @param fonts list of {@link java.awt.Font}.
     */
    public DefaultWordRenderer(List<Color> colors, List<Font> fonts) {
        this.colors.addAll(colors);
        this.fonts.addAll(fonts);
    }

    @Override
    public void render(final String word, BufferedImage image) {
        Graphics2D g = image.createGraphics();

        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);

        FontRenderContext frc = g.getFontRenderContext();
        int xBaseline = (int) Math.round(image.getWidth() * XOFFSET);
        int yBaseline = image.getHeight() - (int) Math.round(image.getHeight() * YOFFSET);

        char[] chars = new char[1];
        for (char c : word.toCharArray()) {
            chars[0] = c;

            g.setColor(colors.get(RAND.nextInt(colors.size())));

            int choiceFont = RAND.nextInt(fonts.size());
            Font font = fonts.get(choiceFont);
            g.setFont(font);

            GlyphVector gv = font.createGlyphVector(frc, chars);
            g.drawChars(chars, 0, chars.length, xBaseline, yBaseline);

            int width = (int) gv.getVisualBounds().getWidth();
            xBaseline = xBaseline + width;
        }
    }
}