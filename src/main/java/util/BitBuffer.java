package main.java.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

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

    public int readBit(InputStream in) throws IOException {
        if (bitsInBuffer == 0) {
            int byteRead = in.read();
            if (byteRead == -1) return -1;  // End of stream
            buffer = byteRead;
            bitsInBuffer = 8;
        }
        int bit = (buffer >> (bitsInBuffer - 1)) & 1;
        bitsInBuffer--;
        if (bitsInBuffer == 0) {
            buffer = 0;  // Clear buffer after reading
        }
        return bit;
    }

    public byte[] toByteArray() {
        int byteLength = (bitsInBuffer + 7) / 8;
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteLength);
        for (int i = 0; i < byteLength; i++) {
            int byteToWrite = buffer >>> (8 * (byteLength - i - 1));
            byteBuffer.put((byte) (byteToWrite & 0xFF));
        }
        return byteBuffer.array();
    }

    public static BitBuffer fromByteArray(byte[] byteArray) {
        BitBuffer bitBuffer = new BitBuffer();
        int buffer = 0;
        for (byte b : byteArray) {
            buffer = (buffer << 8) | (b & 0xFF);
        }
        bitBuffer.buffer = buffer;
        bitBuffer.bitsInBuffer = byteArray.length * 8;
        return bitBuffer;
    }

    private void resetBuffer() {
        buffer = 0;
        bitsInBuffer = 0;
    }

    @Override
    public void close() throws IOException {
        // This method is called when using try-with-resources
        // It doesn't do anything in this implementation, but it's good practice to include it
    }
}

