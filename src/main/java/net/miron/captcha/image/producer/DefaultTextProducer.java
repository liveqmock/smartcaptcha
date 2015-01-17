package net.miron.captcha.image.producer;

import java.util.Random;

/**
 * Produces text of a given length from a given array of characters.
 */
public class DefaultTextProducer implements TextProducer {

    private static final Random RANDOM = new Random();
    private static final int DEFAULT_LENGTH = 5;
    private static final char[] DEFAULT_CHARS = new char[]{'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 'w', 'x', 'y',
            '2', '3', '4', '5', '6', '7', '8',};

    private final String text;

    /**
     * Creates a {@link DefaultTextProducer} with default length and random characters.
     */
    public DefaultTextProducer() {
        text = generateText(DEFAULT_LENGTH, DEFAULT_CHARS);
    }

    /**
     * Creates a {@link DefaultTextProducer} with random characters and specified length.
     * @param length length of text.
     */
    public DefaultTextProducer(int length) {
        text = generateText(length, DEFAULT_CHARS);
    }

    /**
     * Creates a {@link DefaultTextProducer} with specified length and characters.
     * @param length length of text.
     * @param dictionary characters for text.
     */
    public DefaultTextProducer(int length, char[] dictionary) {
        text = generateText(length, dictionary);
    }

    /**
     * Creates a {@link DefaultTextProducer} with specified text.
     * @param text text.
     */
    public DefaultTextProducer(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    private String generateText(int length, char[] dictionary) {
        String txt = "";
        for (int i = 0; i < length; i++) {
            txt += dictionary[RANDOM.nextInt(DEFAULT_LENGTH)];
        }
        return txt;
    }
}
