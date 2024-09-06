
package main.java.util;

import java.io.IOException;
import java.io.OutputStream;

public class BitBuffer implements AutoCloseable {
    private int buffer;
    private int bitsInBuffer;

    public BitBuffer() {
        buffer = 0;
        bitsInBuffer = 0;
    }

    public void writeBit(boolean bit) {
        buffer <<= 1;
        if (bit) {
            buffer |= 1;
        }
        bitsInBuffer++;

        if (bitsInBuffer == 8) {
            resetBuffer();
        }
    }

    public void writeByte(int b) {
        for (int i = 7; i >= 0; i--) {
            writeBit((b & (1 << i)) != 0);
        }
    }

    public void flush(OutputStream out) throws IOException {
        if (bitsInBuffer > 0) {
            int byteToWrite = buffer << (8 - bitsInBuffer);  // Fill up the remaining bits
            out.write(byteToWrite);
        }
        buffer = 0;
        bitsInBuffer = 0;
        out.flush();
    }

    private void resetBuffer() {
        // This method is called internally when the buffer is full
        // It doesn't actually write to an output stream, just resets the buffer
        buffer = 0;
        bitsInBuffer = 0;
    }

    @Override
    public void close() throws IOException {
        // This method is called when using try-with-resources
        // It doesn't do anything in this implementation, but it's good practice to include it
    }
}