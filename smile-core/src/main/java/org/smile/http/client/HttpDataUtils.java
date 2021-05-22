package org.smile.http.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.smile.commons.StringRandom;
import org.smile.io.ByteArrayOutputStream;

public final class HttpDataUtils {
	/**读取一次内容的缓冲区*/
    private static final int bufferSize = 1024*10; // ~10K.
    /**多媒体上传是绑定key的长度*/
    static final int boundaryLength = 32;

    private HttpDataUtils() {}

    /**
     * Writes the input stream to the output stream. Doesn't close them.
     * @param in input stream to read from
     * @param out output stream to write to
     * @throws IOException on IO error
     */
    static void crossStreams(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }


    /**
     * Read the input stream into a byte buffer.
     * @param inStream the input stream to read from
     * @param maxSize the maximum size in bytes to read from the stream. Set to 0 to be unlimited.
     * @return the filled byte buffer
     * @throws IOException if an exception occurs whilst reading from the input stream.
     */
    static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException {
        boolean capped = maxSize > 0;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int read;
        int remaining = maxSize;

        while (true) {
            read = inStream.read(buffer);
            if (read == -1) break;
            if (capped) {
                if (read > remaining) {
                    outStream.write(buffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(buffer, 0, read);
        }
        outStream.close();
        return ByteBuffer.wrap(outStream.toByteArray());
    }

    static ByteBuffer readToByteBuffer(InputStream inStream) throws IOException {
        return readToByteBuffer(inStream, 0);
    }

    static ByteBuffer readFileToByteBuffer(File file) throws IOException {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[(int) randomAccessFile.length()];
            randomAccessFile.readFully(bytes);
            return ByteBuffer.wrap(bytes);
        } finally {
            if (randomAccessFile != null){
                randomAccessFile.close();
            }
        }
    }

    static ByteBuffer emptyByteBuffer() {
        return ByteBuffer.allocate(0);
    }

    /**
     * Creates a random string, suitable for use as a mime boundary
     */
    static String mimeBoundary() {
        return StringRandom.getInstance().randomAlphaNumeric(boundaryLength);
    }
}

