package net.miron.captcha.audio.noise;

import java.util.List;

import net.miron.captcha.audio.Sample;

/**
 * Base interface for the audio noise producer.
 */
public interface NoiseProducer {

    /**
     * Adds noise to the list of specified audio samples.
     * @param target the list of {@link net.miron.captcha.audio.Sample}.
     */
    public Sample addNoise(List<Sample> target);
}
