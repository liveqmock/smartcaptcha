package ml.miron.captcha.audio;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.miron.captcha.image.producer.NumbersAnswerProducer;
import ml.miron.captcha.util.MixerUtil;
import ml.miron.captcha.audio.producer.RandomNoiseProducer;
import ml.miron.captcha.audio.producer.NoiseProducer;
import ml.miron.captcha.audio.producer.NumberVoiceProducer;
import ml.miron.captcha.audio.producer.VoiceProducer;
import ml.miron.captcha.image.producer.TextProducer;

/**
 * A builder for generating a CAPTCHA audio/answer pair.
 * <p>
 * Example for generating a new CAPTCHA:
 * </p>
 * <pre>
 * AudioCaptcha ac = new AudioCaptcha.Builder()
 *   .addAnswer()
 *   .addNoise()
 *   .build();
 * </pre>
 * <p>
 * Note that the <code>build()</code> method must always be called last. Other
 * methods are optional.
 * </p>
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 * @author <a href="mailto:emironen0@gmail.com">Evgeny Mironenko</a>
 */
public final class AudioCaptcha {

    private static final Random RAND = new SecureRandom();

    private final Builder builder;

    /**
     * Creates a {@link AudioCaptcha} with specified instance of {@link AudioCaptcha.Builder}.
     * @param builder builder for captcha.
     */
    private AudioCaptcha(Builder builder) {
        this.builder = builder;
    }

    /**
     * Captcha class builder.
     */
    public static class Builder {

        private String answer = "";
        private Sample challenge;
        private List<VoiceProducer> voiceProds;
        private List<NoiseProducer> noiseProds;

        /**
         * Creates a {@link AudioCaptcha.Builder} with empty voice and noise lists.
         */
        public Builder() {
            voiceProds = new ArrayList<VoiceProducer>();
            noiseProds = new ArrayList<NoiseProducer>();
        }

        /**
         * Add answer to the captcha using the {@link ml.miron.captcha.image.producer.NumbersAnswerProducer}.
         * @return the current instance of {@link AudioCaptcha.Builder}.
         */
        public Builder addAnswer() {
            return addAnswer(new NumbersAnswerProducer());
        }

        /**
         * Add answer to the captcha using the given {@link ml.miron.captcha.image.producer.TextProducer}.
         * @param ansProd the instance of {@link ml.miron.captcha.image.producer.TextProducer}.
         * @return the current instance of {@link AudioCaptcha.Builder}.
         */
        public Builder addAnswer(TextProducer ansProd) {
            answer += ansProd.getText();
            return this;
        }

        /**
         * Add voice using the default {@link NumberVoiceProducer}.
         * @return the current instance of {@link AudioCaptcha.Builder}.
         */
        public Builder addVoice() {
            voiceProds.add(new NumberVoiceProducer());
            return this;
        }

        /**
         * Add voice using the given {@link VoiceProducer}.
         * @param vProd the instance of {@link VoiceProducer}.
         * @return the current instance of {@link AudioCaptcha.Builder}.
         */
        public Builder addVoice(VoiceProducer vProd) {
            voiceProds.add(vProd);
            return this;
        }

        /**
         * Add noise using the default {@link RandomNoiseProducer}.
         * @return the current instance of {@link AudioCaptcha.Builder}.
         */
        public Builder addNoise() {
            return addNoise(new RandomNoiseProducer());
        }

        /**
         * Add voice using the given {@link NoiseProducer}.
         * @param noiseProd the instance of {@link NoiseProducer}.
         * @return the current instance of {@link AudioCaptcha.Builder}.
         */
        public Builder addNoise(NoiseProducer noiseProd) {
            noiseProds.add(noiseProd);
            return this;
        }

        /**
         * Builds the captcha. This method should always be called,
         * and should always be called last.
         * @return The constructed captcha.
         */
        public AudioCaptcha build() {
            // Make sure we have at least one voiceProducer
            if (voiceProds.isEmpty()) {
                addVoice();
            }

            // Convert answer to an array
            char[] answerArray = answer.toCharArray();

            // Make a List of Samples for each character
            VoiceProducer vProd;
            List<Sample> samples = new ArrayList<Sample>();
            Sample sample;
            for (char c : answerArray) {
                // Create Sample for this character from one of the
                // VoiceProducers
                vProd = voiceProds.get(RAND.nextInt(voiceProds.size()));
                sample = vProd.getVocalization(c);
                samples.add(sample);
            }

            // 3. Add noise, if any, and return the result
            if (noiseProds.isEmpty()) {
                NoiseProducer nProd = noiseProds.get(RAND.nextInt(noiseProds.size()));
                challenge = nProd.addNoise(samples);

                return new AudioCaptcha(this);
            }

            challenge = MixerUtil.append(samples);

            return new AudioCaptcha(this);
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
     * Returns the audio sample of captcha.
     * @return see description.
     */
    public Sample getChallenge() {
        return builder.challenge;
    }
}
