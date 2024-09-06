package main.java.huffman;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HuffmanTree {
    private Node root;

    public Node buildTree(Map<Byte, Integer> frequencyMap) {
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        Set<Map.Entry<Byte, Integer>> entrySet = frequencyMap.entrySet();
        for (Map.Entry<Byte, Integer> entry : entrySet) {
            Node node = new Node(entry.getKey(), entry.getValue());
            minHeap.add(node);
        }

        while (minHeap.size() > 1) {
            Node first = minHeap.poll();    
            Node second = minHeap.poll();
            Node newNode = new Node((byte) 0, first.frequency + second.frequency);
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
            out.writeByte(node.character);
        } else {
            out.writeBoolean(false); 
            serialize(node.left, out);  
            serialize(node.right, out);
        }
    }

    public Node deserialize(DataInputStream in) throws IOException {
        boolean isLeaf = in.readBoolean();

        if (isLeaf) {
            
            byte character = in.readByte();
            return new Node((byte) character, 0);
        } else {
            
            Node left = deserialize(in);
            Node right = deserialize(in);
            return new Node(0, left, right); 
        }
    }
}
