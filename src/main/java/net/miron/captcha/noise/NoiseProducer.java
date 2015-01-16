package net.miron.captcha.noise;

import java.awt.image.BufferedImage;

/**
 * Base interface for the image noise producer.
 */
public interface NoiseProducer {

    /**
     * Adds noise to the specified image.
     *
     * @param image the instance of {@link java.awt.image.BufferedImage}
     */
    public void makeNoise(BufferedImage image);
}
