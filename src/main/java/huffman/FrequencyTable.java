package main.java.huffman;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrequencyTable {
    private Map<Byte, Integer> freqmap;

    public FrequencyTable(String filepath) throws IOException {
        freqmap = new HashMap<>();
        countFrequencies(filepath); 
    }

    private void countFrequencies(String filepath) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filepath))) {
            byte[] buffer = new byte[8192]; // 8KB buffer
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte b = buffer[i];
                    freqmap.put(b, freqmap.getOrDefault(b, 0) + 1);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading file : " + filepath, e);
        }
    }

    public Map<Byte, Integer> getFreqmap() {
        return freqmap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Frequency Table:\n");
        for (Map.Entry<Byte, Integer> entry : freqmap.entrySet()) {
            sb.append(String.format("Byte: %d, Frequency: %d\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}
