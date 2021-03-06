package ml.miron.captcha.image.producer;


/**
 * TextProducer implementation that will return a series of numbers.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public class NumbersAnswerProducer implements TextProducer {

    private static final int DEFAULT_LENGTH = 6;
    private static final char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final TextProducer txtProd;

    /**
     * Creates a {@link NumbersAnswerProducer} with default length.
     */
    public NumbersAnswerProducer() {
        this(DEFAULT_LENGTH);
    }

    /**
     * Creates a {@link NumbersAnswerProducer} with specified length.
     * @param length length of text.
     */
    public NumbersAnswerProducer(int length) {
        txtProd = new DefaultTextProducer(length, NUMBERS);
    }

    /**
     * Creates a {@link NumbersAnswerProducer} with specified text.
     * @param text text.
     */
    public NumbersAnswerProducer(String text) {
        txtProd = new DefaultTextProducer(text);
    }

    @Override
    public String getText() {
        return txtProd.getText();
    }
}
