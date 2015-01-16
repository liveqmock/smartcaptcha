package net.miron.captcha.audio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioInputStream;

/**
 * Helper class for operating on {@link Sample}.
 */
public final class MixerUtil {

    private MixerUtil() {
    }

    public static Sample append(List<Sample> samples) {
        if (samples.isEmpty()) {
            return buildSample(0, new double[0]);
        }

        int sampleCount = 0;

        // append voices to each other
        double[] first = samples.get(0).getInterleavedSamples();
        sampleCount += samples.get(0).getSampleCount();

        double[][] samplesArray = new double[samples.size() - 1][];
        for (int i = 0; i < samplesArray.length; i++) {
            samplesArray[i] = samples.get(i + 1).getInterleavedSamples();
            sampleCount += samples.get(i + 1).getSampleCount();
        }

        double[] appended = concatAll(first, samplesArray);

        return buildSample(sampleCount, appended);
    }

    public static Sample mix(Sample sample1, double volAdj1, Sample sample2, double volAdj2) {
        double[] samples1 = sample1.getInterleavedSamples();
        double[] samples2 = sample2.getInterleavedSamples();

        double[] mixed = mix(samples1, volAdj1, samples2, volAdj2);

        return buildSample(sample1.getSampleCount(), mixed);
    }

    private static double[] concatAll(double[] first, double[]... rest) {
        int totalLength = first.length;
        for (double[] array : rest) {
            totalLength += array.length;
        }
        double[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (double[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    private static double[] mix(double[] sample1, double volAdj1, double[] sample2, double volAdj2) {
        for (int i = 0; i < sample1.length; i++) {
            if (i >= sample2.length) {
                sample1[i] = 0;
                break;
            }
            sample1[i] = (sample1[i] * volAdj1) + (sample2[i] * volAdj2);
        }
        return sample1;
    }

    private static AudioInputStream buildStream(long sampleCount, double[] sample) {
        byte[] buffer = Sample.asByteArray(sampleCount, sample);
        InputStream bais = new ByteArrayInputStream(buffer);
        return new AudioInputStream(bais, Sample.SC_AUDIO_FORMAT, sampleCount);
    }

    private static Sample buildSample(long sampleCount, double[] sample) {
        AudioInputStream ais = buildStream(sampleCount, sample);
        return new Sample(ais);
    }
}
