package org.em.miron.captcha.audio.producer;

import org.em.miron.captcha.audio.Sample;

/**
 * Generates a vocalization for a single character.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public interface VoiceProducer {

    /**
     * Returns the audio sample for the given single character.
     * @param letter single character.
     * @return see description.
     */
    public Sample getVocalization(char letter);
}
