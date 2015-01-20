package ml.miron.captcha.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import ml.miron.captcha.audio.Sample;

/**
 * Helper class for operating with {@link javax.servlet.http.HttpServletResponse} and image/audio resources.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public final class CaptchaServletUtil {

    private static final Logger LOG = Logger.getLogger(CaptchaServletUtil.class.getName());
    private static final int AUDIO_STREAM_SIZE = 1024;

    public static final String CAPTCHA_ATTRIBUTE = "CAPTCHA";

    private CaptchaServletUtil() {
    }

    /**
     * Writes image to the response.
     * @param response current response.
     * @param image determined image.
     */
    public static void writeImage(HttpServletResponse response, BufferedImage image) {
        response.setHeader("Cache-Control", "private,no-cache,no-store");
        // PNGs allow for transparency. JPGs does not.
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
            os.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not write image to response: ", e);
        }
    }

    /**
     * Writes audio to the response.
     * @param response current response.
     * @param sample determined audio.
     */
    public static void writeAudio(HttpServletResponse response, Sample sample) {
        response.setHeader("Cache-Control", "private,no-cache,no-store");
        response.setContentType("audio/wav");

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(AUDIO_STREAM_SIZE);
            AudioSystem.write(sample.getAudioInputStream(), AudioFileFormat.Type.WAVE, baos);
            response.setContentLength(baos.size());

            OutputStream os = response.getOutputStream();
            os.write(baos.toByteArray());
            os.flush();
            os.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not write audio to response: ", e);
        }
    }
}
