package org.em.miron.captcha.audio.producer;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.em.miron.captcha.audio.Sample;
import org.em.miron.captcha.util.FileUtil;

/**
 * Generates a vocalization for a given number, randomly selecting from a list of voices.
 * The default voices are located in the jar in the <code>sounds/en/numbers</code> directory,
 * and have filenames with a format of <i>num</i>-<i>voice</i>.wav, e.g.:
 * <code>sounds/en/numbers/1-alex.wav</code>.
 */
public class NumberVoiceProducer implements VoiceProducer {

    private static final Random RAND = new SecureRandom();
    private static final String[] DEFAULT_VOICES = {"alex", "bruce", "fred", "ralph", "kathy", "vicki", "victoria"};
    private static final Map<Integer, String[]> DEFAULT_VOICES_MAP;

    static {
        DEFAULT_VOICES_MAP = new HashMap<>();
        String[] filesForNum;
        StringBuilder sb;

        for (int i = 0; i < 10; i++) {
            filesForNum = new String[DEFAULT_VOICES.length];
            for (int j = 0; j < filesForNum.length; j++) {
                sb = new StringBuilder("/META-INF/resources/smartcaptcha/sounds/en/numbers/");
                sb.append(i);
                sb.append("-");
                sb.append(DEFAULT_VOICES[j]);
                sb.append(".wav");
                filesForNum[j] = sb.toString();
            }
            DEFAULT_VOICES_MAP.put(i, filesForNum);
        }
    }

    private final Map<Integer, String[]> voices;

    /**
     * Creates a {@link NumberVoiceProducer} for the default set of voices.
     */
    public NumberVoiceProducer() {
        this(DEFAULT_VOICES_MAP);
    }

    /**
     * Creates a {@link NumberVoiceProducer} for the given <code>voices</code>.
     * Conceptually the map must look like the following:
     * <pre>
     * {1: ["/my_sounds/1-quiet.wav", "/my_sounds/1-loud.wav"],
     *  2: ["/my_sounds/2-quiet.wav", "/my_sounds/2-loud.wav"]}
     * </pre>
     *
     * @param voices a map of numbers to their corresponding filenames.
     */
    public NumberVoiceProducer(Map<Integer, String[]> voices) {
        this.voices = voices;
    }

    @Override
    public final Sample getVocalization(char num) {
        if (!Character.isDigit(num)) {
            throw new IllegalArgumentException("Expected <num> to be a number, got '" + num + "' instead.");
        }

        int idx = Integer.parseInt(num + "");
        String[] files = voices.get(idx);
        String filename = files[RAND.nextInt(files.length)];

        return FileUtil.readSample(filename);
    }
}
