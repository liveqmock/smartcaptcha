package ml.miron.captcha.image.renderer;

import java.awt.image.BufferedImage;

/**
 * Render the answer for the CAPTCHA into the image.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public interface WordRenderer {

    /**
     * Render a word into a BufferedImage.
     * @param word The sequence of characters to be rendered.
     * @param image The image onto which the word will be rendered.
     */
    public void render(String word, BufferedImage image);

}
