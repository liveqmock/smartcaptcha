package org.em.miron.captcha.image.renderer;

import java.awt.image.BufferedImage;

/**
 * Render the answer for the CAPTCHA into the image.
 */
public interface WordRenderer {

    /**
     * Render a word into a BufferedImage.
     * @param word The sequence of characters to be rendered.
     * @param image The image onto which the word will be rendered.
     */
    public void render(String word, BufferedImage image);

}
