package net.miron.captcha;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.imageio.ImageIO;

import net.miron.captcha.backgrounds.TransparentBackgroundProducer;
import net.miron.captcha.renderer.FishEyeRenderer;
import net.miron.captcha.renderer.Renderer;
import net.miron.captcha.text.renderer.WordRenderer;
import net.miron.captcha.backgrounds.BackgroundProducer;
import net.miron.captcha.noise.CurvedLineNoiseProducer;
import net.miron.captcha.noise.NoiseProducer;
import net.miron.captcha.text.producer.DefaultTextProducer;
import net.miron.captcha.text.producer.TextProducer;
import net.miron.captcha.text.renderer.DefaultWordRenderer;

/**
 * A builder for generating a CAPTCHA image/answer pair.
 * <p/>
 * <p>
 * Example for generating a new CAPTCHA:
 * </p>
 * <pre>Captcha nl.captcha = new Captcha.Builder(200, 50)
 * 	.addText()
 * 	.addBackground()
 * 	.build();</pre>
 * <p>Note that the <code>build()</code> must always be called last. Other methods are optional,
 * and can sometimes be repeated. For example:</p>
 * <pre>Captcha nl.captcha = new Captcha.Builder(200, 50)
 * 	.addText()
 * 	.addNoise()
 * 	.addNoise()
 * 	.addNoise()
 * 	.addBackground()
 * 	.build();</pre>
 * <p>Adding multiple backgrounds has no affect; the last background added will simply be the
 * one that is eventually rendered.</p>
 */
public final class Captcha implements Serializable {

    private static final long serialVersionUID = 617511236L;

    private final Builder builder;

    private Captcha(Builder builder) {
        this.builder = builder;
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = 1223993387863964106L;

        private String answer = "";
        private BufferedImage img;
        private BufferedImage bg;
        private Date timeStamp;
        private boolean addBorder = false;

        public Builder(int width, int height) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        /**
         * Add a background using the default {@link TransparentBackgroundProducer}).
         */
        public Builder addBackground() {
            return addBackground(new TransparentBackgroundProducer());
        }

        /**
         * Add a background using the given {@link BackgroundProducer}.
         *
         * @param bgProd the instance of {@link BackgroundProducer}.
         */
        public Builder addBackground(BackgroundProducer bgProd) {
            bg = bgProd.getBackground(img.getWidth(), img.getHeight());
            return this;
        }

        public Builder addText(WordRenderer wordRenderer) {
            return addText(new DefaultTextProducer(), wordRenderer);
        }

        /**
         * Generate the answer to the CAPTCHA using the {@link DefaultTextProducer}.
         */
        public Builder addText() {
            return addText(new DefaultTextProducer());
        }

        /**
         * Generate the answer to the CAPTCHA using the given
         * {@link TextProducer}.
         *
         * @param txtProd the instance of {@link TextProducer}.
         */
        public Builder addText(TextProducer txtProd) {
            return addText(txtProd, new DefaultWordRenderer());
        }

        /**
         * Generate the answer to the CAPTCHA using the given
         * {@link TextProducer}, and render it to the image using the given
         * {@link WordRenderer}.
         *
         * @param txtProd   the instance of {@link TextProducer}.
         * @param wRenderer the instance of {@link WordRenderer}.
         */
        public Builder addText(TextProducer txtProd, WordRenderer wRenderer) {
            answer += txtProd.getText();
            wRenderer.render(answer, img);
            return this;
        }

        /**
         * Add noise using the default {@link CurvedLineNoiseProducer}.
         */
        public Builder addNoise() {
            return this.addNoise(new CurvedLineNoiseProducer());
        }

        /**
         * Add noise using the given {@link NoiseProducer}.
         *
         * @param nProd the instance of {@link NoiseProducer}.
         */
        public Builder addNoise(NoiseProducer nProd) {
            nProd.makeNoise(img);
            return this;
        }

        /**
         * Gimp the image using the default {@link net.miron.captcha.renderer.FishEyeRenderer}.
         */
        public Builder gimp() {
            return gimp(new FishEyeRenderer());
        }

        /**
         * Gimp the image using the given {@link net.miron.captcha.renderer.Renderer}.
         *
         * @param renderer the instance of {@link net.miron.captcha.renderer.Renderer}.
         */
        public Builder gimp(Renderer renderer) {
            renderer.gimp(img);
            return this;
        }

        /**
         * Draw a single-pixel wide black border around the image.
         */
        public Builder addBorder() {
            addBorder = true;

            return this;
        }

        /**
         * Build the CAPTCHA. This method should always be called, and should always
         * be called last.
         *
         * @return The constructed CAPTCHA.
         */
        public Captcha build() {
            if (bg == null) {
                bg = new TransparentBackgroundProducer().getBackground(img.getWidth(), img.getHeight());
            }

            // Paint the main image over the background
            Graphics2D g = bg.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g.drawImage(img, null, null);

            if (addBorder) {
                int width = img.getWidth();
                int height = img.getHeight();

                g.setColor(Color.BLACK);
                g.drawLine(0, 0, 0, width);
                g.drawLine(0, 0, width, 0);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(width - 1, height - 1, width - 1, 0);
            }

            img = bg;
            timeStamp = new Date();

            return new Captcha(this);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(answer);
            out.writeObject(timeStamp);
            ImageIO.write(img, "png", ImageIO.createImageOutputStream(out));
        }

        private void readObject(ObjectInputStream in ) throws IOException, ClassNotFoundException {
            answer = (String) in .readObject();
            timeStamp = (Date) in .readObject();
            img = ImageIO.read(ImageIO.createImageInputStream( in ));
        }
    }

    public boolean isCorrect(String answer) {
        return answer.equals(builder.answer);
    }

    public String getAnswer() {
        return builder.answer;
    }

    /**
     * Get the CAPTCHA image, a PNG.
     *
     * @return A PNG CAPTCHA image.
     */
    public BufferedImage getImage() {
        return builder.img;
    }
}