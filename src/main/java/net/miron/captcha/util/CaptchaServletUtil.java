package net.miron.captcha.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import net.miron.captcha.audio.Sample;

public final class CaptchaServletUtil {

    public static final String CAPTCHA_ATTRIBUTE = "CAPTCHA";

    public static void writeImage(HttpServletResponse response, BufferedImage bi) {
        response.setHeader("Cache-Control", "private,no-cache,no-store");
        response.setContentType("image/png"); // PNGs allow for transparency.
                                              // JPGs do not.
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(bi, "png", os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAudio(HttpServletResponse response, Sample sample) {
        response.setHeader("Cache-Control", "private,no-cache,no-store");
        response.setContentType("audio/wav");

        try {
            // Convert to BAOS so we can set the content-length header
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            AudioSystem.write(sample.getAudioInputStream(), AudioFileFormat.Type.WAVE, baos);
            response.setContentLength(baos.size());

            OutputStream os = response.getOutputStream();
            os.write(baos.toByteArray());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
