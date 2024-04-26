package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        sortedCharFreqList = new ArrayList<>();
        StdIn.setFile(fileName);
        double total = 0; // 10
        int[] ASCII = new int[128];
        Character tempChar = null;

        // Counts TOTAL number of Chars in File
        while(StdIn.hasNextChar() == true){
            StdIn.readChar();
            total++;
        }

        // Inputs Each Character and their number of Occurences into ASCII
        StdIn.setFile(fileName);
        while(StdIn.hasNextChar() == true){
            tempChar = StdIn.readChar();
            // System.out.println(tempChar);
            ASCII[tempChar]++;
        }

        // Special Case
        int current = 0;
        int c;
        int index_of_char = 0;
        for(c = 0; c<ASCII.length; c++){
            if(ASCII[c] != 0){
                index_of_char = c;
                current++;
            }
        }
        if (current == 1){
            char dummyChar = (char)(index_of_char+1);
            CharFreq dummyObject = new CharFreq(dummyChar,0);
            sortedCharFreqList.add(dummyObject);
        }


        for (int i=0; i< ASCII.length; i++){
            if(ASCII[i] != 0){
            char tempC = (char)(i);
            Character C = Character.valueOf(tempC);
            CharFreq charObject = new CharFreq(C, ASCII[i]/total);
            sortedCharFreqList.add(charObject);
            }
        }
        Collections.sort(sortedCharFreqList);
    }
    

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     * Corner Cases: If the target queue's empty, if there's one item in each, if the source is empty and other ones.
    1. Start two empty queues: Source and Target
    2. Create a node for each character present in the input file,
    each node contains the character and its occurrence probability. 
    3. Enqueue the nodes in the Source queue in increasing order of occurrence probability.
    4. Repeat until the Source queue is empty and the Target queue has only one node.
        1. Dequeue from either queue or both the two nodes with the smallest occurrence probability. 
        If the front node of Source and Target have the same occurrence probability, dequeue from Source first.
        2. Create a new node whose character is null and occurrence probability is the sum of the
        occurrence probabilities of the two dequeued nodes. Add the two dequeued nodes as children:
        the first dequeued node as the left child and the second dequeued node as the right child.
        3. Enqueue the new node into Target.

    Step 4.1 – do the following procedure twice: compare the probability occurrences of the front nodes of Source and Target. 
    If they are equal or if Source is less, dequeue Source. If the Target is less, dequeue Target.
    Step 4.2 – the first dequeued node and second dequeued node should be left and right children respectively.
     */
    public void makeTree() {
        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        // Source Queue Population
        for(int i = 0; i < sortedCharFreqList.size(); i++){
            TreeNode temp = new TreeNode(sortedCharFreqList.get(i), null, null);
            source.enqueue(temp);
        }

        TreeNode firstNode = new TreeNode();
        TreeNode secondNode = new TreeNode();

        // Target is Empty (Beginning)
        if(target.isEmpty()){
            firstNode = source.dequeue();
            secondNode = source.dequeue();
            CharFreq charSumNode = new CharFreq(null, firstNode.getData().getProbOcc() + secondNode.getData().getProbOcc());
            TreeNode sumNode = new TreeNode(charSumNode, firstNode, secondNode);
            target.enqueue(sumNode);
        }
        while(!source.isEmpty()){
            for (int i = 0; i < 2; i++){
                if (i == 0){
                    if (source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                        firstNode = source.dequeue();
                        continue;
                    }
                    else if (source.peek().getData().getProbOcc() > target.peek().getData().getProbOcc()){
                        firstNode = target.dequeue();
                        continue;
                    }
                }
                if (i == 1){
                    if (!source.isEmpty() && !target.isEmpty()){
                        if (source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                            secondNode = source.dequeue();
                            continue;
                        }
                        else if (source.peek().getData().getProbOcc() > target.peek().getData().getProbOcc()){
                            secondNode = target.dequeue();
                            continue;
                        }   
                    }else if(source.isEmpty()){
                        secondNode = target.dequeue();
                        continue;
                    }else if(target.isEmpty()){
                        secondNode = source.dequeue();
                        continue;
                    }          
                }
            }
            CharFreq charSumNode = new CharFreq(null, firstNode.getData().getProbOcc() + secondNode.getData().getProbOcc());
            TreeNode sumNode = new TreeNode(charSumNode, firstNode, secondNode);
            target.enqueue(sumNode);
        }
        if (source.isEmpty() && target.size() >= 2){
            while (target.size() > 1){
                firstNode = target.dequeue();
                secondNode = target.dequeue();
                CharFreq charSumNode = new CharFreq(null, firstNode.getData().getProbOcc() + secondNode.getData().getProbOcc());
                TreeNode sumNode = new TreeNode(charSumNode, firstNode, secondNode);
                target.enqueue(sumNode);
            }
        }
        huffmanRoot = target.peek();
    }

    private void huffmanCode(TreeNode root, String s, String[] ASCII){
        if (root != null){
            if (root.getLeft() == null && root.getRight() == null){
                ASCII[root.getData().getCharacter()] = s;
            }   
            huffmanCode(root.getLeft(), s + "0",ASCII);
            huffmanCode(root.getRight(), s + "1",ASCII);
        }
        if (root == null) { return; }
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        String[] ASCII = new String[128];
        huffmanCode(huffmanRoot, "", ASCII);
        encodings = ASCII;
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        String s = "";
        StdIn.setFile(fileName);
        while(StdIn.hasNextChar() == true){
            s = s + encodings[StdIn.readChar()];
        }
        writeBitString(encodedFile, s);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        TreeNode pointer = huffmanRoot;
        String s = readBitString(encodedFile);
        StdOut.setFile(decodedFile);
        for(int i = 0; i < s.length(); i++){
            if(huffmanRoot != null){
                if(s.charAt(i) == '0'){
                    pointer = pointer.getLeft();
                }else if(s.charAt(i) == '1'){
                    pointer = pointer.getRight();
                }
            }
            if (pointer.getLeft() == null && pointer.getRight() == null){
                StdOut.print(pointer.getData().getCharacter());
                pointer = huffmanRoot;
            }
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
