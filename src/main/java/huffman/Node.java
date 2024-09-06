package main.java.huffman;

public class Node implements Comparable<Node>{

    byte character;
    int frequency;
    Node left, right;

    public Node(byte character, int frequency){
        this.character = character;
        this.frequency = frequency;
        this.left = this.right = null;
    }

    public Node(int frequency, Node left, Node right){
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.frequency, other.frequency);

//        alternate way below which avoids overflow for very large frequencies
//        but unlikely we will get a very large frequency
//        if (n.freq < this.freq) {
//            return 1;
//        } else if (n.freq > this.freq) {
//            return -1;
//        } else {
//            return 0;
//        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "character=" + character +
                ", frequency=" + frequency +
                '}';
    }
}
