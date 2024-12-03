package main.java.huffman;

import main.java.util.BitBuffer;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {

    private Map<Character, String> huffmanCodeMap;
    private HuffmanTree huffmanTree;

    public HuffmanEncoder() {
        huffmanCodeMap = new HashMap<>();
        huffmanTree = new HuffmanTree();
    }

    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        // Step 1: Generate Frequency Table
        FrequencyTable frequencyTable = new FrequencyTable(inputFilePath);
        Map<Byte, Integer> frequencyMap = frequencyTable.getFreqmap();

        // Step 2: Build Huffman Tree
        Node root = huffmanTree.buildTree(convertToCharFrequencyMap(frequencyMap));

        // Step 3: Generate Huffman Codes
        generateHuffmanCodes(root, "");

        // Step 4: Compress Data
        StringBuilder encodedData = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFilePath))) {
            int b;
            while ((b = bis.read()) != -1) {
                encodedData.append(huffmanCodeMap.get((char) b));
            }
        }

        // Step 5: Write Output (Compressed data)
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
             DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)) {

            // Serialize the Huffman tree to the file
            huffmanTree.serialize(root, dataOutputStream);

            // Write compressed data using BitBuffer
            try (BitBuffer bitBuffer = new BitBuffer()) {
                for (char bit : encodedData.toString().toCharArray()) {
                    bitBuffer.writeBit(bit == '1');
                }
                bitBuffer.flush(dataOutputStream);
            }
        }
    }

    // Helper function to generate Huffman codes by traversing the tree
    private void generateHuffmanCodes(Node node, String code) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            huffmanCodeMap.put(node.character, code);
        }

        generateHuffmanCodes(node.left, code + "0");
        generateHuffmanCodes(node.right, code + "1");
    }

    // Convert frequency map from Byte to Character
    private Map<Character, Integer> convertToCharFrequencyMap(Map<Byte, Integer> byteFreqMap) {
        Map<Character, Integer> charFreqMap = new HashMap<>();
        for (Map.Entry<Byte, Integer> entry : byteFreqMap.entrySet()) {
            charFreqMap.put((char) entry.getKey().byteValue(), entry.getValue());
        }
        return charFreqMap;
    }
}
