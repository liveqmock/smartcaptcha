package net.miron.captcha.image.renderer;

import java.awt.image.BufferedImage;

/**
 * Base interface for the graphic image renderer.
 */
public interface Renderer {

    /**
     * Draws the graphic image based on the defined {@value BufferedImage}.
     * @param image the filled instance of {@link BufferedImage}.
     */
    public void gimp(BufferedImage image);
}
