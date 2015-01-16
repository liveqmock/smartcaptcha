package net.miron.captcha.audio.producer;

import net.miron.captcha.audio.Sample;

/**
 * Generates a vocalization for a single character.
 */
public interface VoiceProducer {

    /**
     * Returns the instance of {@link net.miron.captcha.audio.Sample} for the given single character.
     *
     * @param letter single character.
     * @return see description.
     */
    public Sample getVocalization(char letter);
}
