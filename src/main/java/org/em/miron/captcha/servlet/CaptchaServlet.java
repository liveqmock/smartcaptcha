package org.em.miron.captcha.servlet;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.em.miron.captcha.image.Captcha;
import org.em.miron.captcha.image.background.GradiatedBackground;
import org.em.miron.captcha.image.producer.DefaultTextProducer;
import org.em.miron.captcha.image.producer.NumbersAnswerProducer;
import org.em.miron.captcha.image.renderer.ColoredEdgesWordRenderer;
import org.em.miron.captcha.util.CaptchaServletUtil;

import static org.em.miron.captcha.util.CaptchaServletUtil.CAPTCHA_ATTRIBUTE;


/**
 * Generates a new {@link Captcha} and writes the image to the response.
 */
public class CaptchaServlet extends HttpServlet implements SingleThreadModel {

    private static final long serialVersionUID = -818415138453710213L;

    private static final List<Color> COLORS = new ArrayList<>();
    private static final List<Font> FONTS = new ArrayList<>();
    private int width = 200;
    private int height = 50;

    static {
        COLORS.add(Color.BLUE);
        COLORS.add(Color.RED);
        COLORS.add(Color.GREEN);

        FONTS.add(new Font("Geneva", Font.ITALIC, 48));
        FONTS.add(new Font("Courier", Font.BOLD, 48));
        FONTS.add(new Font("Arial", Font.BOLD, 48));
    }

    @Override
    public void init() throws ServletException {
        if (getInitParameter("captcha-height") != null) {
            height = Integer.parseInt(getInitParameter("captcha-height"));
        }

        if (getInitParameter("captcha-width") != null) {
            width = Integer.parseInt(getInitParameter("captcha-width"));
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ColoredEdgesWordRenderer wordRenderer = new ColoredEdgesWordRenderer(COLORS, FONTS);
        Captcha.Builder builder = new Captcha.Builder(width, height).gimp().addNoise().addBackground(new GradiatedBackground());
        String answer = (String) req.getSession().getAttribute(CAPTCHA_ATTRIBUTE);
        if (answer != null) {
            builder.addText(new DefaultTextProducer(answer), wordRenderer);
        } else {
            builder.addText(new NumbersAnswerProducer(), wordRenderer);
        }
        Captcha captcha = builder.build();
        req.getSession().setAttribute(CAPTCHA_ATTRIBUTE, captcha.getAnswer());
        CaptchaServletUtil.writeImage(resp, captcha.getImage());
    }
}