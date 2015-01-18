package org.em.miron.captcha.image.background;

import java.awt.image.BufferedImage;

/**
 * Abstract implementation of {@link Background}.
 *
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public abstract class AbstractBackground implements Background {

    @Override
    public BufferedImage addBackground(BufferedImage image) {
        return getBackground(image.getWidth(), image.getHeight());
    }
}
