package ml.miron.captcha.image.producer;

import java.awt.image.BufferedImage;

/**
 * Base interface for the image noise producer.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public interface NoiseProducer {

    /**
     * Adds noise to the specified image.
     * @param image the instance of {@link java.awt.image.BufferedImage}
     */
    public void makeNoise(BufferedImage image);
}
