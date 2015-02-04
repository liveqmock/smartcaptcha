package ml.miron.captcha.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ml.miron.captcha.image.background.Background;
import ml.miron.captcha.image.producer.DefaultTextProducer;
import ml.miron.captcha.image.producer.NoiseProducer;
import ml.miron.captcha.image.renderer.DefaultWordRenderer;
import ml.miron.captcha.image.renderer.FishEyeRenderer;
import ml.miron.captcha.image.renderer.Renderer;
import ml.miron.captcha.image.renderer.WordRenderer;
import ml.miron.captcha.image.background.TransparentBackground;
import ml.miron.captcha.image.producer.CurvedLineNoiseProducer;
import ml.miron.captcha.image.producer.TextProducer;

/**
 * A builder for generating a captcha image/answer pair.
 * <p>
 * Example for generating a new captcha:
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
 * <p>Adding multiple background has no affect; the last background added will simply be the
 * one that is eventually rendered.</p>
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public final class Captcha {

    private final Builder builder;

    /**
     * Creates a {@link Captcha} with specified instance of {@link Captcha.Builder}.
     * @param builder builder for captcha.
     */
    private Captcha(Builder builder) {
        this.builder = builder;
    }

    /**
     * Captcha class builder.
     */
    public static class Builder {

        private String answer = "";
        private BufferedImage image;
        private BufferedImage background;
        private boolean addBorder = false;

        /**
         * Creates a {@link Captcha.Builder} with specified width and height.
         * @param width width of captcha.
         * @param height height of captcha.
         */
        public Builder(int width, int height) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        /**
         * Add a background using the default {@link TransparentBackground}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addBackground() {
            return addBackground(new TransparentBackground());
        }

        /**
         * Add a background using the given {@link ml.miron.captcha.image.background.Background}.
         * @param background the instance of {@link ml.miron.captcha.image.background.Background}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addBackground(Background background) {
            this.background = background.getBackground(image.getWidth(), image.getHeight());
            return this;
        }

        /**
         * Add answer to the captcha with specified instance of {@link ml.miron.captcha.image.renderer.WordRenderer}.
         * @param wordRenderer the instance of {@link ml.miron.captcha.image.renderer.WordRenderer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addText(WordRenderer wordRenderer) {
            return addText(new DefaultTextProducer(), wordRenderer);
        }

        /**
         * Add answer to the captcha using the {@link DefaultTextProducer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addText() {
            return addText(new DefaultTextProducer());
        }

        /**
         * ADd answer to the captcha using the given {@link ml.miron.captcha.image.producer.TextProducer}.
         * @param txtProd the instance of {@link ml.miron.captcha.image.producer.TextProducer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addText(TextProducer txtProd) {
            return addText(txtProd, new DefaultWordRenderer());
        }

        /**
         * ADd answer to the captcha using the given
         * {@link TextProducer}, and render it to the image using the given
         * {@link WordRenderer}.
         * @param txtProd the instance of {@link TextProducer}.
         * @param wRenderer the instance of {@link WordRenderer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addText(TextProducer txtProd, WordRenderer wRenderer) {
            answer += txtProd.getText();
            wRenderer.render(answer, image);
            return this;
        }

        /**
         * Add noise using the default {@link CurvedLineNoiseProducer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addNoise() {
            return this.addNoise(new CurvedLineNoiseProducer());
        }

        /**
         * Add noise using the given {@link ml.miron.captcha.image.producer.NoiseProducer}.
         * @param noise the instance of {@link ml.miron.captcha.image.producer.NoiseProducer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addNoise(NoiseProducer noise) {
            noise.makeNoise(image);
            return this;
        }

        /**
         * Gimp the image using the default {@link ml.miron.captcha.image.renderer.FishEyeRenderer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder gimp() {
            return gimp(new FishEyeRenderer());
        }

        /**
         * Gimp the image using the given {@link ml.miron.captcha.image.renderer.Renderer}.
         * @param renderer the instance of {@link ml.miron.captcha.image.renderer.Renderer}.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder gimp(Renderer renderer) {
            renderer.gimp(image);
            return this;
        }

        /**
         * Draw a single-pixel wide black border around the image.
         * @return the current instance of {@link Captcha.Builder}.
         */
        public Builder addBorder() {
            addBorder = true;
            return this;
        }

        /**
         * Builds the captcha. This method should always be called,
         * and should always be called last.
         * @return The constructed captcha.
         */
        public Captcha build() {
            if (background == null) {
                background = new TransparentBackground().getBackground(image.getWidth(), image.getHeight());
            }

            // Paint the main image over the background
            Graphics2D g = background.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g.drawImage(image, null, null);

            if (addBorder) {
                int width = image.getWidth();
                int height = image.getHeight();

                g.setColor(Color.BLACK);
                g.drawLine(0, 0, 0, width);
                g.drawLine(0, 0, width, 0);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(width - 1, height - 1, width - 1, 0);
            }

            image = background;

            return new Captcha(this);
        }
    }

    /**
     * Returns true if specified answer if correct, otherwise false.
     * @param answer answer for captcha.
     * @return see description.
     */
    public boolean isCorrect(String answer) {
        return answer.equals(builder.answer);
    }

    /**
     * Returns the answer for captcha.
     * @return see description.
     */
    public String getAnswer() {
        return builder.answer;
    }

    /**
     * Get the PNG captcha image.
     * @return see description.
     */
    public BufferedImage getImage() {
        return builder.image;
    }
}