package main.java.huffman;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HuffmanTree {
    private Node root;

    public Node buildTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<Node> minHeap = new PriorityQueue<>();

        Set<Map.Entry<Character, Integer>> entrySet = frequencyMap.entrySet();
        for (Map.Entry<Character, Integer> entry : entrySet) {
            Node node = new Node(entry.getKey(), entry.getValue());
            minHeap.add(node);
        }

        while (minHeap.size() > 1) {
            Node first = minHeap.poll();    
            Node second = minHeap.poll();
            Node newNode = new Node('\0', first.frequency + second.frequency);
            newNode.left = first;
            newNode.right = second;
            minHeap.add(newNode);
        }

        root = minHeap.poll();  
        return root;
    }

    public void serialize(Node node, DataOutputStream out) throws IOException {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            out.writeBoolean(true);  
            out.writeChar(node.character); 
        } else {
            out.writeBoolean(false); 
            serialize(node.left, out);  
            serialize(node.right, out);
        }
    }

    public Node deserialize(DataInputStream in) throws IOException {
        boolean isLeaf = in.readBoolean();

        if (isLeaf) {
            
            char character = in.readChar();
            return new Node(character, 0); 
        } else {
            
            Node left = deserialize(in);
            Node right = deserialize(in);
            return new Node(0, left, right); 
        }
    }
}
