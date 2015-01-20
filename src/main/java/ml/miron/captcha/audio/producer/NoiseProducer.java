package ml.miron.captcha.audio.producer;

import java.util.List;

import ml.miron.captcha.audio.Sample;

/**
 * Base interface for the audio noise producer.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public interface NoiseProducer {

    /**
     * Append the given <code>samples</code> to each other, then add random
     * noise to the result.
     * @param target list of {@link ml.miron.captcha.audio.Sample}.
     * @return prepared instance of {@link ml.miron.captcha.audio.Sample}.
     */
    public Sample addNoise(List<Sample> target);
}
