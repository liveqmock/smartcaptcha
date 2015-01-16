package net.miron.captcha.audio.noise;

import java.util.List;

import net.miron.captcha.audio.Sample;

/**
 * Base interface for the audio noise producer.
 */
public interface NoiseProducer {

    /**
     * Append the given <code>samples</code> to each other, then add random
     * noise to the result.
     * @param target the list of {@link net.miron.captcha.audio.Sample}.
     * @return
     */
    public Sample addNoise(List<Sample> target);
}
