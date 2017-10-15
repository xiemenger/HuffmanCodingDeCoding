/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 4-way heap seems to the fastest on my laptop...
 */
public class HuffmanEncoder {
    private static final String codeTableFile = "code_table.txt";
    private static final String encodedFile = "encoded.bin";
    private static final int maxTreeDepth = 100;
    private static final boolean debug = false;
    private static HashMap codeTable = null;
    private static DaryNode root = null;
    private static long startTime;
    private static long endTime;
    
    public static void setRoot(DaryNode r) {
        root = r;
    }
    
    public static void buildCodeTable() {
        if (root == null) {
            System.out.println("Error: no huffman tree found");
            return;
        }
        codeTable = new HashMap();
        int[] path = new int[maxTreeDepth];
        int depth = 0;
        
        // Traverse the huffman tree
        traverseAndBuild(root, path, depth);
        
        // Debug
        if (debug) {
            System.out.println("Debug: code table:");
            dumpCodeTable();
        }
    }
    
    public static void encode(String input) {
        if (codeTable == null) {
            System.out.println("Error: no code table found");
            return;
        }
        
        // Open the input (again) and 2 outputs
        File file = new File(input);
        BufferedReader reader = null;
        PrintWriter txtWriter = null;
        FileOutputStream binOut = null;
        String text;
        String code;

        try {
            reader = new BufferedReader(new FileReader(file));
            txtWriter = new PrintWriter(codeTableFile);
            binOut = new FileOutputStream(encodedFile);
            ArrayList<String> codeList = new ArrayList<>();
            byte[] codeBin;
            // Encode the input
            System.out.println("Start encoding...");
            startTime = System.nanoTime();
            while ((text = reader.readLine()) != null) {
                if (text.isEmpty()) {
                    continue;
                }
                codeList.add((String) codeTable.get(Integer.parseInt(text)));
                //endTime = System.nanoTime();
                //System.out.println("Encoding done (microsecond) - " + ((endTime - startTime)/1000));
            
                // Appened the encoded file
                //codeBin = genCodeBin(code);
                //binOut.write(codeBin);
            }
            // Build the big string
            StringBuilder sb = new StringBuilder();
            for (String s : codeList)
                sb.append(s);
            // Write the encoded file once
            // Since we need to write multiple times of 8 bits (1 byte)
            codeBin = genCodeBin(sb.toString());
            binOut.write(codeBin);
            endTime = System.nanoTime();
            System.out.println("Encoding done (microsecond) - " + ((endTime - startTime)/1000));
            
            // Write the code table
            Set set = codeTable.entrySet();
            Iterator i = set.iterator();
            Map.Entry me;
            while(i.hasNext()) {
                me = (Map.Entry)i.next();
                //System.out.print(me.getKey() + ": ");
                //System.out.println(me.getValue());
                txtWriter.println(me.getKey() + " " + me.getValue());
            }
            
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (txtWriter != null)
                    txtWriter.close();
                if (binOut != null)
                    binOut.close();
            } catch (IOException e) {
            }
        }
    }
    
    private static void traverseAndBuild(DaryNode n, int[] path, int depth) {
        // Debug
        if (debug)
            System.out.println("Debug: " + n);
        
        // Assign left edge 0
        if (n.left != null) {
            path[depth] = 0;
            traverseAndBuild(n.left, path, depth+1);
        }
        
        // Assign right edge 1
        if (n.right != null) {
            path[depth] = 1;
            traverseAndBuild(n.right, path, depth+1);
        }
        
        // Process the leaf
        if (n.isLeaf()) {
            String encoding = "";
            int i;
            for (i = 0; i < depth; i++)
                encoding += path[i];
            codeTable.put(n.num, encoding);
        }
    }
    
    private static void dumpCodeTable() {
        Set set = codeTable.entrySet();
        Iterator i = set.iterator();
        Map.Entry me;
        while(i.hasNext()) {
            me = (Map.Entry)i.next();
                System.out.print(me.getKey() + ": ");
                System.out.println(me.getValue());
        }       
    }
    
    private static byte[] genCodeBin(String code) {
        int binLen;
        int remLen;
        String code2 = code;
        
        remLen = code.length() % 8;
        if (remLen == 0) {
            binLen = code.length()/8;
        } else {
            System.out.println("Warning: encoding needs to pad " + (8-remLen) + " bits for the last byte");
            binLen = code.length()/8 + 1;
            // Pad (8-remLen) 0s
            int i;
            for (i = 0; i < (8-remLen); i++)
                code2 += "0";
        }
        
        // Convert to byte
        byte[] binCode = new byte[binLen];
        int idx = 0;
        String value;
        int i;
        for (i = 0; i < binLen; i++) {
            value = code2.substring(idx, idx+8);
            binCode[i] = (byte) Integer.parseInt(value, 2);
            idx += 8;
        }
        
        return binCode;
    }
}
