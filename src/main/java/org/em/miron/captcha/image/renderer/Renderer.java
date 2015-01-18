package org.em.miron.captcha.image.renderer;

import java.awt.image.BufferedImage;

/**
 * Base interface for the graphic image renderer.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public interface Renderer {

    /**
     * Draws the graphic image based on the defined {@link BufferedImage}.
     * @param image the filled instance of {@link BufferedImage}.
     */
    public void gimp(BufferedImage image);
}
