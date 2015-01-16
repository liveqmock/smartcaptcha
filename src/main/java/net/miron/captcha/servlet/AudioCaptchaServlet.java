package net.miron.captcha.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.miron.captcha.audio.AudioCaptcha;
import net.miron.captcha.text.producer.DefaultTextProducer;
import net.miron.captcha.util.CaptchaServletUtil;

import static net.miron.captcha.util.CaptchaServletUtil.CAPTCHA_ATTRIBUTE;

/**
 * Generates a new {@link AudioCaptcha} and writes the audio to the response.
 */
public class AudioCaptchaServlet extends HttpServlet implements SingleThreadModel {

    private static final long serialVersionUID = 4690256047223360039L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AudioCaptcha.Builder builder = new AudioCaptcha.Builder().addNoise();
        String answer = (String) req.getSession().getAttribute(CAPTCHA_ATTRIBUTE);
        if (answer != null) {
            builder.addAnswer(new DefaultTextProducer(answer));
        } else {
            builder.addAnswer();
        }
        AudioCaptcha captcha = builder.build();
        req.getSession().setAttribute(CAPTCHA_ATTRIBUTE, captcha.getAnswer());
        CaptchaServletUtil.writeAudio(resp, captcha.getChallenge());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
