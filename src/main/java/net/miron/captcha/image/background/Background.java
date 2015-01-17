package net.miron.captcha.image.background;

import java.awt.image.BufferedImage;

/**
 * Base interface for background.
 */
public interface Background {

    //TODO miron: add default implementation of method.
    /**
     * Add the background to the given image.
     * @param image The image onto which the background will be rendered.
     * @return The image with the background rendered.
     */
    public BufferedImage addBackground(BufferedImage image);

    /**
     * Returns the background by the given width and height.
     * @param width  width in pixels.
     * @param height height in pixels.
     * @return see description.
     */
    public BufferedImage getBackground(int width, int height);
}