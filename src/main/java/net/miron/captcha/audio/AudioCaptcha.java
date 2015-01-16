package net.miron.captcha.audio;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.miron.captcha.audio.noise.RandomNoiseProducer;
import net.miron.captcha.audio.noise.NoiseProducer;
import net.miron.captcha.audio.producer.RandomNumberVoiceProducer;
import net.miron.captcha.audio.producer.VoiceProducer;
import net.miron.captcha.text.producer.NumbersAnswerProducer;
import net.miron.captcha.text.producer.TextProducer;

/**
 * A builder for generating a CAPTCHA audio/answer pair.
 *
 * <p>
 * Example for generating a new CAPTCHA:
 * </p>
 * 
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
 */
public final class AudioCaptcha {

    private static final Random RAND = new SecureRandom();

    private final Builder builder;

    private AudioCaptcha(Builder builder) {
        this.builder = builder;
    }

    public static class Builder {

        private String answer = "";
        private Sample challenge;
        private List<VoiceProducer> voiceProds;
        private List<NoiseProducer> noiseProds;

        public Builder() {
            voiceProds = new ArrayList<>();
            noiseProds = new ArrayList<>();
        }

        public Builder addAnswer() {
            return addAnswer(new NumbersAnswerProducer());
        }

        public Builder addAnswer(TextProducer ansProd) {
            answer += ansProd.getText();
            return this;
        }

        public Builder addVoice() {
            voiceProds.add(new RandomNumberVoiceProducer());
            return this;
        }

        public Builder addVoice(VoiceProducer vProd) {
            voiceProds.add(vProd);
            return this;
        }

        public Builder addNoise() {
            return addNoise(new RandomNoiseProducer());
        }

        public Builder addNoise(NoiseProducer noiseProd) {
            noiseProds.add(noiseProd);
            return this;
        }

        public AudioCaptcha build() {
            // Make sure we have at least one voiceProducer
            if (voiceProds.isEmpty()) {
                addVoice();
            }

            // Convert answer to an array
            char[] answerArray = answer.toCharArray();

            // Make a List of Samples for each character
            VoiceProducer vProd;
            List<Sample> samples = new ArrayList<>();
            Sample sample;
            for (char c: answerArray) {
                // Create Sample for this character from one of the
                // VoiceProducers
                vProd = voiceProds.get(RAND.nextInt(voiceProds.size()));
                sample = vProd.getVocalization(c);
                samples.add(sample);
            }

            // 3. Add noise, if any, and return the result
            if (noiseProds.isEmpty()) {
                NoiseProducer nProd = noiseProds.get(RAND.nextInt(noiseProds
                        .size()));
                challenge = nProd.addNoise(samples);

                return new AudioCaptcha(this);
            }

            challenge = Mixer.append(samples);

            return new AudioCaptcha(this);
        }
    }

    public boolean isCorrect(String answer) {
        return answer.equals(builder.answer);
    }

    public String getAnswer() {
        return builder.answer;
    }

    public Sample getChallenge() {
        return builder.challenge;
    }
}
