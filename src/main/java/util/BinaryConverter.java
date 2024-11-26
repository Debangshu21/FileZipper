package main.java.util;

public class BinaryConverter {

    // Convert a character to its binary string representation (Huffman code)
    public static String charToBinaryString(char c) {
        return Integer.toBinaryString(c);
    }

    // Convert a binary string back to its character using Huffman cod
    public static char binaryStringToChar(String binary) {
        return (char) Integer.parseInt(binary, 2);
    }

    // Convert a byte to a bit string (e.g., 00001101)
    public static String byteToBitString(byte b) {
        StringBuilder bitString = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            bitString.append((b >> i) & 1);
        }
        return bitString.toString();
    }

    // Pad a binary string to a byte boundary (add leading zeroes if necessary)
    public static String padBinaryString(String binary, int byteBoundary) {
        while (binary.length() % byteBoundary != 0) {
            binary = "0" + binary;
        }
        return binary;
    }
}
