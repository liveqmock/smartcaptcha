package org.em.miron.captcha.audio.producer;

import org.em.miron.captcha.audio.Sample;

/**
 * Generates a vocalization for a single character.
 */
public interface VoiceProducer {

    /**
     * Returns the audio sample for the given single character.
     * @param letter single character.
     * @return see description.
     */
    public Sample getVocalization(char letter);
}
