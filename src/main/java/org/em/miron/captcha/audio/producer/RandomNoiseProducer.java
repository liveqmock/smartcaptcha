package org.em.miron.captcha.audio.producer;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.em.miron.captcha.util.MixerUtil;
import org.em.miron.captcha.audio.Sample;
import org.em.miron.captcha.util.FileUtil;

/**
 * Adds noise to a {@link org.em.miron.captcha.audio.Sample} from one of the given <code>noiseFiles</code>.
 * By default this noise comes from one of three files, all located in
 * <code>/sounds/noises/</code>: <code>radio_tuning.wav</code>,
 * <code>restaurant.wav</code>, and <code>swimming.wav</code>. This can be
 * overridden by passing the location of your own sound files to the
 * constructor, e.g.:
 * <pre>
 * String myFiles = { &quot;/mysounds/noise1.wav&quot;, &quot;/mysounds/noise2.wav&quot; };
 * NoiseProducer myNp = new RandomNoiseProducer(myFiles);
 * </pre>
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public class RandomNoiseProducer implements NoiseProducer {

    private static final Random RAND = new SecureRandom();
    private static final String[] DEFAULT_NOISES = {
            "/META-INF/resources/smartcaptcha/sounds/sounds/noises/radio_tuning.wav",
            "/META-INF/resources/smartcaptcha/sounds/sounds/noises/restaurant.wav",
            "/META-INF/resources/smartcaptcha/sounds/sounds/noises/swimming.wav",};

    private final String[] noiseFiles;

    /**
     * Creates a {@link RandomNoiseProducer} from default set of noises.
     */
    public RandomNoiseProducer() {
        this(DEFAULT_NOISES);
    }

    /**
     * Creates a {@link RandomNoiseProducer} from one of the given <code>noiseFiles</code>.
     * @param noiseFiles array of paths to files.
     */
    public RandomNoiseProducer(String[] noiseFiles) {
        this.noiseFiles = noiseFiles.clone();
    }

    @Override
    public Sample addNoise(List<Sample> samples) {
        Sample appended = MixerUtil.append(samples);
        String noiseFile = noiseFiles[RAND.nextInt(noiseFiles.length)];
        Sample noise = FileUtil.readSample(noiseFile);

        // Decrease the volume of the noise to make sure the voices can be heard
        return MixerUtil.mix(appended, 1.0, noise, 0.6);
    }
}
