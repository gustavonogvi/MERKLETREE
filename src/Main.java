import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

class Node {
    Node left, right; //left and right children
    String value, content; //hash value and content
    boolean wasCopied; //indicates if the node is a copy

    Node(Node left, Node right, String value, String content, boolean wasCopied) {
        this.left = left;
        this.right = right;
        this.value = value;
        this.content = content;
        this.wasCopied = wasCopied;
    }

    //method to compute the hash using the Tiger algorithm
    static String hash(String val) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("Tiger");
        byte[] hash = digest.digest(val.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //method to copy the current node
    Node copy() {
        return new Node(this.left, this.right, this.value, this.content, true);
    }
}

class MerkleTree {
    Node root; //root of the Merkle Tree

    //constructor to build the Merkle Tree
    MerkleTree(List<String> values) throws NoSuchAlgorithmException {
        this.root = buildTree(values);
    }

    //method to build the tree from a list of values
    Node buildTree(List<String> values) throws NoSuchAlgorithmException {
        List<Node> leaves = new ArrayList<>();
        //create leaf nodes
        for (String e : values) {
            leaves.add(new Node(null, null, Node.hash(e), e, false));
        }
        //duplicate the last node if the number of leaves is odd
        if (leaves.size() % 2 == 1) {
            leaves.add(leaves.get(leaves.size() - 1).copy());
        }
        return buildTreeRec(leaves);
    }

    //recursive method to build the tree
    Node buildTreeRec(List<Node> nodeList) throws NoSuchAlgorithmException {
        //duplicate the last node if the number of nodes is odd
        if (nodeList.size() % 2 == 1) {
            nodeList.add(nodeList.get(nodeList.size() - 1).copy());
        }
        int half = nodeList.size() / 2;

        //base case: if there are only two nodes, create the root
        if (nodeList.size() == 2) {
            return new Node(nodeList.get(0), nodeList.get(1),
                    Node.hash(nodeList.get(0).value + nodeList.get(1).value),
                    nodeList.get(0).content + "+" + nodeList.get(1).content, false);
        }

        //recursively build the left and right subtrees
        Node left = buildTreeRec(nodeList.subList(0, half));
        Node right = buildTreeRec(nodeList.subList(half, nodeList.size()));
        String value = Node.hash(left.value + right.value);
        String content = left.content + "+" + right.content;
        return new Node(left, right, value, content, false);
    }

    //method to print the tree
    void printTree() { printTreeRec(this.root); }

    //recursive method to print the tree
    void printTreeRec(Node node) {
        if (node != null) {
            if (node.left != null) {
                System.out.println("Left: " + node.left.value);
                System.out.println("Right: " + node.right.value);
            } else {
                System.out.println("Entry");
            }

            if (node.wasCopied) {
                System.out.println("(phantom element)");
            }
            System.out.println("Value: " + node.value);
            System.out.println("Content: " + node.content);
            System.out.println("");
            printTreeRec(node.left);
            printTreeRec(node.right);
        }
    }

    //method to get the root hash
    String getRootHash() { return this.root.value; }
}

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        //add BouncyCastle as a security provider
        Security.addProvider(new BouncyCastleProvider());

        //file paths
        String filePath = "C:\\Users\\gusta\\Desktop\\dataHash.txt";
        String rootHashPath = "C:\\Users\\gusta\\Desktop\\rootHash.txt";

        //read data from the file
        List<String> elements = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                elements.add(line);
            }
        }

        System.out.println("Input: " + new StringJoiner(", ").add(elements.toString()));
        System.out.println("\n");

        //build the Merkle tree and print it
        MerkleTree tree = new MerkleTree(elements);
        tree.printTree();

        //get the new root hash
        String newRootHash = tree.getRootHash();
        System.out.println("New Root Hash: " + newRootHash + "\n");

        //check if the new root hash matches the original root hash
        String originalRootHash = null;
        try {
            originalRootHash = loadRootHash(rootHashPath);
            System.out.println("Original Root Hash: " + originalRootHash);
            if (originalRootHash.equals(newRootHash)) {
                System.out.println("INTEGRITY APPROVED!");
            } else {
                System.out.println("DATA IS NOT INTACT!");
            }
        } catch (IOException e) {
            System.out.println("Root hash file not found.");
        }

        //save the new root hash if it doesn't match the original
        if (originalRootHash == null || !originalRootHash.equals(newRootHash)) {
            saveRootHash(rootHashPath, newRootHash);
            System.out.println("Hash added to the file.");
        }
    }

    //method to save the root hash to a file
    public static void saveRootHash(String outputPath, String hash) throws IOException {
        Files.writeString(Paths.get(outputPath), hash, StandardOpenOption.CREATE);
    }

    //method to load the root hash from a file
    public static String loadRootHash(String inputPath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(inputPath)), StandardCharsets.UTF_8).trim();
    }
}
