package net.miron.captcha.audio;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.*;

/**
 * Class representing a sound sample, typically read in from a file. Note that
 * at this time this class only supports wav files with the following
 * characteristics:
 * <ul>
 * <li>Sample rate: 16KHz</li>
 * <li>Sample size: 16 bits</li>
 * <li>Channels: 1</li>
 * <li>Signed: true</li>
 * <li>Big Endian: false</li>
 * </ul>
 * <p/>
 * <p>
 * Data files in other formats will cause an
 * <code>IllegalArgumentException</code> to be thrown.
 * </p>
 */
public class Sample {

    private static final Logger LOG = Logger.getLogger(Sample.class.getName());

    public static final AudioFormat SC_AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false);

    private final AudioInputStream audioInputStream;

    /**
     * Creates a {@link Sample} with defined input stream.
     * @param is input stream.
     */
    public Sample(InputStream is) {
        if (is instanceof AudioInputStream) {
            audioInputStream = (AudioInputStream) is;
            return;
        }

        try {
            audioInputStream = AudioSystem.getAudioInputStream(is);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IllegalStateException(e);
        }

        checkFormat(audioInputStream.getFormat());
    }

    /**
     * Returns audio stream.
     * @return see description.
     */
    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    /**
     * Returns the current format audio.
     * @return see description.
     */
    public AudioFormat getFormat() {
        return audioInputStream.getFormat();
    }

    /**
     * Return the number of samples of all channels.
     * @return see description.
     */
    public long getSampleCount() {
        long total = (audioInputStream.getFrameLength()
                * getFormat().getFrameSize() * 8)
                / getFormat().getSampleSizeInBits();
        return total / getFormat().getChannels();
    }

    /**
     * Returns the array of interleaved samples.
     * @return see description.
     */
    public double[] getInterleavedSamples() {
        double[] samples = new double[(int) getSampleCount()];
        try {
            getInterleavedSamples(0, getSampleCount(), samples);
        } catch (IllegalArgumentException | IOException e) {
            LOG.log(Level.SEVERE, "Could not get interleaved samples: ", e);
        }

        return samples;
    }

    /**
     * Get the interleaved decoded samples for all channels, from sample index
     * <code>begin</code> (included) to sample index <code>end</code> (excluded)
     * and copy them into <code>samples</code>. <code>end</code> must not exceed
     * <code>getSampleCount()</code>, and the number of samples must not be so
     * large that the associated byte array cannot be allocated.
     */
    public double[] getInterleavedSamples(long begin, long end, double[] samples) throws IOException {
        long nbSamples = end - begin;
        long nbBytes = nbSamples * (getFormat().getSampleSizeInBits() / 8) * getFormat().getChannels();
        if (nbBytes > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Too many samples. Try using a smaller wav.");
        }
        // allocate a byte buffer
        byte[] inBuffer = new byte[(int) nbBytes];
        // read bytes from audio file
        audioInputStream.read(inBuffer, 0, inBuffer.length);
        // decode bytes into samples.
        decodeBytes(inBuffer, samples);

        return samples;
    }

    /**
     * Extract samples of a particular channel from interleavedSamples and copy
     * them into channelSamples.
     */
    public void getChannelSamples(int channel, double[] interleavedSamples, double[] channelSamples) {
        int nbChannels = getFormat().getChannels();
        for (int i = 0; i < channelSamples.length; i++) {
            channelSamples[i] = interleavedSamples[nbChannels * i + channel];
        }
    }

    /**
     * Convenience method. Extract left and right channels for common stereo
     * files. leftSamples and rightSamples must be of size getSampleCount().
     */
    public void getStereoSamples(double[] leftSamples, double[] rightSamples)
            throws IOException {
        long sampleCount = getSampleCount();
        double[] interleavedSamples = new double[(int) sampleCount * 2];
        getInterleavedSamples(0, sampleCount, interleavedSamples);
        for (int i = 0; i < leftSamples.length; i++) {
            leftSamples[i] = interleavedSamples[2 * i];
            rightSamples[i] = interleavedSamples[2 * i + 1];
        }
    }

    /**
     * Decodes bytes of audioBytes into audioSamples.
     */
    public void decodeBytes(byte[] audioBytes, double[] audioSamples) {
        int sampleSizeInBytes = getFormat().getSampleSizeInBits() / 8;
        int[] sampleBytes = new int[sampleSizeInBytes];
        int k = 0;
        for (int i = 0; i < audioSamples.length; i++) {
            // collect sample byte in big-endian order
            if (getFormat().isBigEndian()) {
                // bytes start with MSB
                for (int j = 0; j < sampleSizeInBytes; j++) {
                    sampleBytes[j] = audioBytes[k++];
                }
            } else {
                // bytes start with LSB
                for (int j = sampleSizeInBytes - 1; j >= 0; j--) {
                    sampleBytes[j] = audioBytes[k++];
                }
            }
            // get integer value from bytes
            int ival = 0;
            for (int j = 0; j < sampleSizeInBytes; j++) {
                ival += sampleBytes[j];
                if (j < sampleSizeInBytes - 1) {
                    ival <<= 8;
                }
            }
            // decode value
            double ratio = Math.pow(2., getFormat().getSampleSizeInBits() - 1);
            double val = ((double) ival) / ratio;
            audioSamples[i] = val;
        }
    }

    /**
     * Return the interleaved samples as a <code>byte[]</code>.
     * @return The interleaved samples.
     */
    public final byte[] asByteArray() {
        return asByteArray(getSampleCount(), getInterleavedSamples());
    }

    /**
     * Helper method to convert a double[] to a byte[] in a format that can be
     * used by {@link AudioInputStream}. Typically this will be used with
     * a {@link Sample} that has been modified from its original.
     * @return A byte[] representing a sample.
     */
    public static byte[] asByteArray(long sampleCount, double[] sample) {
        int bufferLength = (int) sampleCount * (SC_AUDIO_FORMAT.getSampleSizeInBits() / 8);
        byte[] buffer = new byte[bufferLength];

        int in;
        for (int i = 0; i < sample.length; i++) {
            in = (int) (sample[i] * 32767);
            buffer[2 * i] = (byte) (in & 255);
            buffer[2 * i + 1] = (byte) (in >> 8);
        }

        return buffer;
    }

    @Override
    public String toString() {
        return "[Sample] samples: " + getSampleCount() + ", format: "
                + getFormat();
    }

    private static void checkFormat(AudioFormat af) {
        if (!af.matches(SC_AUDIO_FORMAT)) {
            throw new IllegalArgumentException("Unsupported audio format.\nReceived: " + af.toString()
                    + "\nExpected: " + SC_AUDIO_FORMAT);
        }
    }
}
