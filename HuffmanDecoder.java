/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class HuffmanDecoder {
    private static final String decodedFile = "decoded.txt";
    private static final boolean debug = false;
    private static HashMap reverseCodeTable;
    private static String code = "";
    private static DaryNode root;
    private static long startTime;
    private static long endTime;
    
    public static void decode(String encodedFile, String codeTableFile) {
        // Load the encoded file
        startTime = System.nanoTime();
        loadEncodedFile(encodedFile);
        endTime = System.nanoTime();
        System.out.println("Loading encoded file done (microsecond) - " + ((endTime - startTime)/1000));
        if (debug)
            System.out.println("Debug: code=" + code);
        
        // Load the code table file
        startTime = System.nanoTime();
        loadCodeTableFile(codeTableFile);
        endTime = System.nanoTime();
        System.out.println("Loading code table file done (microsecond) - " + ((endTime - startTime)/1000));
        if (debug)
            dumpReverseCodeTable();
        
        // Build the decode tree
        startTime = System.nanoTime();
        buildDecodeTree();
        endTime = System.nanoTime();
        System.out.println("Building decode tree done (microsecond) - " + ((endTime - startTime)/1000));
        if (debug)
            HuffmanTree.dumpHuffmanTreeCacheHeap(root, 0);
        
        // Decode to the output
        startTime = System.nanoTime();
        try {
            PrintWriter txtWriter = new PrintWriter(decodedFile);
            char[] codeArray = code.toCharArray();
            DaryNode n = root;
            int i;
            for (i = 0; i < codeArray.length; i++) {
                // Choose the edge
                if (codeArray[i] == '0')
                    n = n.left;
                else
                    n = n.right;
                
                // Check for leaf node
                if (n.freq != -1) {
                    if (debug)
                        System.out.println("Debug: writing " + n.freq);
                    txtWriter.println(n.freq);
                    n = root;
                }
            }
            
            // NOTE: this damn close is REALLY needed!
            txtWriter.close();
            endTime = System.nanoTime();
            System.out.println("Decoding done (microsecond) - " + ((endTime - startTime)/1000));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HuffmanDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void loadEncodedFile(String input) {
        try {
            // NOTE: JDK 7+
            Path path = Paths.get(input);
            byte[] binCode =  Files.readAllBytes(path);
            StringBuilder sb = new StringBuilder();
            
            // Get each bit
            // To handle a big string (>25 MB)
            // we should use string builder rather than overloading the string pool
            int i, j, k;
            for (i = 0; i < binCode.length; i++) {
                for (j = 0; j < 8; j++) {
                    k = (binCode[i] & (1 << (7-j))) >> (7-j);
                    if (k == 1)
                        //code += "1";
                        sb.append("1");
                    else
                        //code += "0";
                        sb.append("0");
                }
            }
            code = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(HuffmanDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void loadCodeTableFile(String input) {
        // Read the file
        File file = new File(input);
        BufferedReader reader = null;
        reverseCodeTable = new HashMap();
        String text;

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((text = reader.readLine()) != null) {
                if (text.isEmpty()) {
                    continue;
                }
                
                // Format: num bin
                int idx = text.indexOf(" ");
                String num = text.substring(0, idx);
                String bin = text.substring(idx+1);
                reverseCodeTable.put(bin, num);                
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }
    
    /*
     * DFS: when depth of the tree is << n, O(n)
     * when depth of the tree is close to n, O(n^2)
     */
    private static void buildDecodeTree() {
        // Create the root
        root = new DaryNode(-1, -1, true);
        
        // Go thru all the codes
        Set set = reverseCodeTable.entrySet();
        Iterator i = set.iterator();
        Map.Entry me;
        while(i.hasNext()) {
            me = (Map.Entry)i.next();
            //System.out.print(me.getKey() + ": ");
            //System.out.println(me.getValue());
            String path = (String) me.getKey();
            char[] pathArray = path.toCharArray();
            // Go thru each edge
            int j;
            DaryNode n = root;
            for (j = 0; j < pathArray.length; j++) {
                // Create the left branch
                if (pathArray[j] == '0') {
                    if (n.left == null) {
                        n.left = new DaryNode(-1, -1, true);
                    }
                    n = n.left;
                }
                
                // Create the right branch
                if (pathArray[j] == '1') {
                    if (n.right == null) {
                        n.right = new DaryNode(-1, -1, true);
                    }
                    n = n.right;
                }
            } 
            // We have the leaf node now
            n.setFreq(Integer.parseInt((String) me.getValue()));
        }
    }
    
    private static void dumpReverseCodeTable() {
        Set set = reverseCodeTable.entrySet();
        Iterator i = set.iterator();
        Map.Entry me;
        while(i.hasNext()) {
            me = (Map.Entry)i.next();
                System.out.print(me.getKey() + ": ");
                System.out.println(me.getValue());
        }       
    }
}
