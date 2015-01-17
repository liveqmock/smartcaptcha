package org.em.miron.captcha.image.producer;


/**
 * Generate an answer for the CAPTCHA.
 */
public interface TextProducer {

    /**
     * Generate a series of characters to be used as the answer for the CAPTCHA.
     * @return The answer for the CAPTCHA.
     */
    public String getText();
}
