package org.em.miron.captcha.image.producer;


/**
 * Generate an answer for the CAPTCHA.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public interface TextProducer {

    /**
     * Generate a series of characters to be used as the answer for the CAPTCHA.
     * @return The answer for the CAPTCHA.
     */
    public String getText();
}
