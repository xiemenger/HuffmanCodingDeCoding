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
import java.util.Arrays;

/**
 *
 */
public class encoder {

    // UT
    private static final boolean enableUT = false;
    private static final boolean binaryHeapTest = false;
    private static final boolean cacheHeapTest = false;
    private static final boolean pairingHeapTest = false;
    // For a min heap
    // (1, 2), (8, 2), (9, 4), (11, 4), (3, 5), (4, 7)
    // (2, 8), (5, 9), (10, 9), (6, 11), (7, 13), (0, 17)
    private static final int[] utData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private static final int[] utFreq = {17, 2, 8, 5, 7, 9, 11, 13, 2, 4, 9, 4};

    // Perf
    private static final boolean enablePerf = false;
    private static final int freqTableLen = 1000000;
    private static final int runTimes = 10;
    private static int[] freqTable;
    private static long startTime;
    private static long endTime;

    // Encode
    private static final boolean enableEncode = true;

    // Decode
    private static final boolean enableDecode = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // UTs
        if (enableUT) {
            if (binaryHeapTest) {
                testBinaryHeap();
            }
            if (cacheHeapTest) {
                testCacheHeap();
            }
            if (pairingHeapTest) {
                testPairingHeap();
            }
        }

        // Build freq table
        if (enablePerf || enableEncode) {
            System.out.println("Building freq table using " + args[0]);
            startTime = System.nanoTime();
            buildFreqTable(args[0]);
            endTime = System.nanoTime();
            System.out.println("Building freq table done (microsecond) - " + ((endTime - startTime)/1000));
        }

        // Measure the perfs
        if (enablePerf) {
            int i;
            startTime = System.nanoTime();
            for (i = 0; i < runTimes; i++) {
                HuffmanTree.buildTreeUsingBinaryHeap(freqTable, freqTableLen);
            }
            endTime = System.nanoTime();
            System.out.println("Time using binary heap (microsecond): " + ((endTime - startTime) / (1000 * runTimes)));

            startTime = System.nanoTime();
            for (i = 0; i < runTimes; i++) {
                HuffmanTree.buildTreeUsingCacheHeap(freqTable, freqTableLen);
            }
            endTime = System.nanoTime();
            System.out.println("Time using 4-way heap (microsecond): " + ((endTime - startTime) / (1000 * runTimes)));

            startTime = System.nanoTime();
            for (i = 0; i < runTimes; i++) {
                HuffmanTree.buildTreeUsingPairingHeap(freqTable, freqTableLen);
            }
            endTime = System.nanoTime();
            System.out.println("Time using pairing heap (microsecond): " + ((endTime - startTime) / (1000 * runTimes)));
        }

        // Encode
        if (enableEncode) {
            // Build a 4-way heap
            startTime = System.nanoTime();
            DaryNode root = HuffmanTree.buildTreeUsingCacheHeap(freqTable, freqTableLen);
            endTime = System.nanoTime();
            System.out.println("Building huffman tree done (microsecond) - " + ((endTime - startTime)/1000));
            // Set the root
            HuffmanEncoder.setRoot(root);
            // Build the code table
            startTime = System.nanoTime();
            HuffmanEncoder.buildCodeTable();
            endTime = System.nanoTime();
            System.out.println("Building code table done (microsecond) - " + ((endTime - startTime)/1000));
            // Encode
            HuffmanEncoder.encode(args[0]);
            System.out.println("Encoding done");
        }
        
        // Decode
        if (enableDecode) {
            HuffmanDecoder.decode(args[0], args[1]);
        }

    }

    // Perfs
    private static void buildFreqTable(String input) {
        String text;
        // Init the table
        freqTable = new int[freqTableLen];
        Arrays.fill(freqTable, 0);

        // Read the file
        File file = new File(input);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((text = reader.readLine()) != null) {
                if (text.isEmpty()) {
                    continue;
                }
                freqTable[Integer.parseInt(text)]++;
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

    // UTs
    // Verified
    public static void testBinaryHeap() {
        DaryNode n;
        // Create a binary heap
        System.out.println("UT: creating binary heap...");
        DaryHeap binHeap = new DaryHeap(utData.length, 2);

        // Add data
        int i;
        System.out.println("UT: adding data...");
        for (i = 0; i < utData.length; i++) {
            binHeap.add(utData[i], utFreq[i], false);
            binHeap.printHeap();
            System.out.println("-----------");
        }

        // Remove data
        System.out.println("UT: removing data...");
        while (!binHeap.isEmpty()) {
            n = binHeap.remove();
            System.out.println("removed: " + n);
            binHeap.printHeap();
            System.out.println("------------");
        }
        System.out.println("UT: done");
    }

    // Verified
    public static void testCacheHeap() {
        DaryNode n;
        // Create a cache heap
        System.out.println("UT: creating 4-way cache heap...");
        DaryHeap cacheHeap = new DaryHeap(utData.length, 4);

        // Add data
        int i;
        System.out.println("UT: adding data...");
        for (i = 0; i < utData.length; i++) {
            cacheHeap.add(utData[i], utFreq[i], false);
            cacheHeap.printHeap();
            System.out.println("-----------");
        }

        // Remove data
        System.out.println("UT: removing data...");
        while (!cacheHeap.isEmpty()) {
            n = cacheHeap.remove();
            System.out.println("removed: " + n);
            cacheHeap.printHeap();
            System.out.println("------------");
        }
        System.out.println("UT: done");
    }

    // Verified
    public static void testPairingHeap() {
        PairNode n;
        // Create a pairing heap
        System.out.println("UT: creating pairing heap...");
        PairingHeap pairHeap = new PairingHeap(utData.length);

        // Add data
        int i;
        System.out.println("UT: adding data...");
        for (i = 0; i < utData.length; i++) {
            pairHeap.add(utData[i], utFreq[i], false);
            pairHeap.printHeap();
            System.out.println("-----------");
        }

        // Remove data
        System.out.println("UT: removing data...");
        while (!pairHeap.isEmpty()) {
            n = pairHeap.remove();
            System.out.println("removed: " + n);
            pairHeap.printHeap();
            System.out.println("------------");
        }
        System.out.println("UT: done");
    }

}
