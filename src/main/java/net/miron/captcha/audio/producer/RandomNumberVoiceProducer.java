package net.miron.captcha.audio.producer;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.miron.captcha.audio.Sample;
import net.miron.captcha.util.FileUtil;

/**
 * <p>
 * {@link VoiceProducer} which generates a vocalization for a given number,
 * randomly selecting from a list of voices. The default voices are located in
 * the jar in the <code>sounds/en/numbers</code> directory, and have filenames
 * with a format of <i>num</i>-<i>voice</i>.wav, e.g.:
 * <code>sounds/en/numbers/1-alex.wav</code>.
 * </p>
 */
public class RandomNumberVoiceProducer implements VoiceProducer {

    private static final Random RAND = new SecureRandom();
    private static final String[] DEFAULT_VOICES = { "alex", "bruce", "fred", "ralph", "kathy", "vicki", "victoria" };
    private static final Map<Integer, String[]> DEFAULT_VOICES_MAP;

    static {
        DEFAULT_VOICES_MAP = new HashMap<>();
        String[] files_for_num;
        StringBuilder sb;
        
        for (int i = 0; i < 10; i++) {
            files_for_num = new String[DEFAULT_VOICES.length];
            for (int j = 0; j < files_for_num.length; j ++) {
                sb = new StringBuilder("/sounds/en/numbers/");
                sb.append(i);
                sb.append("-");
                sb.append(DEFAULT_VOICES[j]);
                sb.append(".wav");
                files_for_num[j] = sb.toString();
            }
            DEFAULT_VOICES_MAP.put(i, files_for_num);
        }
    }

    private final Map<Integer, String[]> voices;

    public RandomNumberVoiceProducer() {
        this(DEFAULT_VOICES_MAP);
    }

    /**
     * Creates a <code>RandomNumberVoiceProducer</code> for the given <code>voices</code>.
     * Conceptually the map must look like the following:
     * 
     * <pre>
     * {1 => ["/my_sounds/1-quiet.wav", "/my_sounds/1-loud.wav"],
     *  2 => ["/my_sounds/2-quiet.wav", "/my_sounds/2-loud.wav"]}
     * </pre>
     * 
     * @param voices a map of numbers to their corresponding filenames.
     */
    public RandomNumberVoiceProducer(Map<Integer, String[]> voices) {
        this.voices = voices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Sample getVocalization(char num) {
        try {
            Integer.parseInt(num + "");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Expected <num> to be a number, got '" + num + "' instead.",
                    e);
        }

        int idx = Integer.parseInt(num + "");
        String[] files = voices.get(idx);
        String filename = files[RAND.nextInt(files.length)];

        return FileUtil.readSample(filename);
    }
}
