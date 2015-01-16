package net.miron.captcha.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.miron.captcha.audio.Sample;

public final class FileUtil {

    private static final Logger LOG = Logger.getLogger(FileUtil.class.getName());

    private FileUtil() {
    }

    /**
     * Get a file resource and return it as an InputStream. Intended primarily
     * to read in binary files which are contained in a jar.
     *
     * @param filename name of file.
     * @return An @{link InputStream} to the file.
     */
    public static InputStream readResource(String filename) {
        InputStream jarIs = FileUtil.class.getResourceAsStream(filename);
        if (jarIs == null) {
            throw new IllegalStateException(new FileNotFoundException("File '" + filename + "' not found."));
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        int nRead;

        try {
            while ((nRead = jarIs.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            jarIs.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not read file: ", e);
        }

        return new ByteArrayInputStream(buffer.toByteArray());
    }

    public static Sample readSample(String filename) {
        InputStream is = readResource(filename);
        return new Sample(is);
    }
}
