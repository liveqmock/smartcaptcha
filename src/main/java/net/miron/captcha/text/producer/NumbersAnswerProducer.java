package net.miron.captcha.text.producer;


/**
 * TextProducer implementation that will return a series of numbers.
 */
public class NumbersAnswerProducer implements TextProducer {

    private static final int DEFAULT_LENGTH = 5;
    private static final char[] NUMBERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private final TextProducer txtProd;
    
    public NumbersAnswerProducer() {
        this(DEFAULT_LENGTH);
    }

    public NumbersAnswerProducer(String answer) {
        txtProd = new DefaultTextProducer(answer);
    }
    
    public NumbersAnswerProducer(int length) {
        txtProd = new DefaultTextProducer(length, NUMBERS);
    }

    @Override
    public String getText() {
        return txtProd.getText();
    }
}
