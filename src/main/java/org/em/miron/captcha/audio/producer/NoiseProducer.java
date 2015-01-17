package org.em.miron.captcha.audio.producer;

import java.util.List;

import org.em.miron.captcha.audio.Sample;

/**
 * Base interface for the audio noise producer.
 */
public interface NoiseProducer {

    /**
     * Append the given <code>samples</code> to each other, then add random
     * noise to the result.
     * @param target list of {@link org.em.miron.captcha.audio.Sample}.
     * @return prepared instance of {@link org.em.miron.captcha.audio.Sample}.
     */
    public Sample addNoise(List<Sample> target);
}
