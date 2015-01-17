package org.em.miron.captcha.image.background;

import java.awt.image.BufferedImage;

/**
 * Abstract implementation of {@link Background}.
 */
public abstract class AbstractBackground implements Background {

    @Override
    public BufferedImage addBackground(BufferedImage image) {
        return getBackground(image.getWidth(), image.getHeight());
    }
}
