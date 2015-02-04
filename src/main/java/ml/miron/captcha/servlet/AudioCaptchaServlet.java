package ml.miron.captcha.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ml.miron.captcha.audio.AudioCaptcha;
import ml.miron.captcha.audio.producer.NumberVoiceProducer;
import ml.miron.captcha.audio.producer.VoiceProducer;
import ml.miron.captcha.image.producer.DefaultTextProducer;
import ml.miron.captcha.util.CaptchaServletUtil;

import static ml.miron.captcha.util.CaptchaServletUtil.CAPTCHA_ATTRIBUTE;

/**
 * Generates a new {@link AudioCaptcha} and writes the audio to the response.
 *
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public class AudioCaptchaServlet extends HttpServlet implements SingleThreadModel {

    private VoiceProducer voiceProducer;

    @Override
    public void init() throws ServletException {
        if (getInitParameter("audio-path") != null) {
            fillVoiceProducer(getInitParameter("audio-path"));
        } else {
            voiceProducer = new NumberVoiceProducer();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AudioCaptcha.Builder builder = new AudioCaptcha.Builder().addNoise().addVoice(voiceProducer);
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

    /**
     * Fills voice producer by fragments located in specified directory.
     * This fragments must have a strictly defined names: 0.wav, 1.wav, 2.wav, ..., 9.wav
     * If even one file does not exists then {@link java.lang.IllegalArgumentException} will be thrown.
     * @param audioDir directory with audio fragments.
     */
    private void fillVoiceProducer(String audioDir) {
        Map<Integer, String[]> voiceMap = new HashMap<Integer, String[]>();
        String fileName;
        for (int i = 0; i < 10; i++) {
            fileName = audioDir + "/" + i + ".wav"; // Example: /bla-bla-bla/0.wav
            if (getClass().getResource(fileName) == null) {
                throw new IllegalArgumentException("File with next name does not exists: " + fileName);
            }
            voiceMap.put(i, new String[]{fileName});
        }
        voiceProducer = new NumberVoiceProducer(voiceMap);
    }
}
