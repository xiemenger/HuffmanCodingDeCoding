/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

/**
 *
 */
public class HuffmanTree {
    //private static HuffmanNode root;
    private static DaryHeap binaryHeap;
    private static DaryHeap cacheHeap;
    private static PairingHeap pairingHeap;
    private static DaryNode binaryRoot;
    private static DaryNode cacheRoot;
    private static PairNode pairRoot;
    private static final boolean debug = false;
    
    public static DaryNode buildTreeUsingBinaryHeap(int[] freqTable, int size) {
        // Create a binary heap
        binaryHeap = new DaryHeap(size, 2);
        
        // Load the data
        int i;
        for (i = 0; i < size; i++) {
            if (freqTable[i] != 0)
                binaryHeap.add(i, freqTable[i], false);
        }
        
        // Build the tree
        DaryNode left;
        DaryNode right;
        DaryNode top;
        int freqSum;
        while (!binaryHeap.isSizeOne()) {
            left = binaryHeap.remove();
            right = binaryHeap.remove();
            freqSum = left.freq + right.freq;
            top = new DaryNode(-1, freqSum, true);
            top.setLR(left, right);
            binaryHeap.addNode(top);
        }
        
        // Return the root
        binaryRoot = binaryHeap.remove();
        return binaryRoot;
    }
    
    public static DaryNode buildTreeUsingCacheHeap(int[] freqTable, int size) {
        // Create a 4-way heap
        cacheHeap = new DaryHeap(size, 4);
        
        // Load the data
        int i;
        for (i = 0; i < size; i++) {
            if (freqTable[i] != 0)
                cacheHeap.add(i, freqTable[i], false);
        }
        
        // Build the tree
        DaryNode left;
        DaryNode right;
        DaryNode top;
        int freqSum;
        while (!cacheHeap.isSizeOne()) {
            left = cacheHeap.remove();
            right = cacheHeap.remove();
            freqSum = left.freq + right.freq;
            top = new DaryNode(-1, freqSum, true);
            top.setLR(left, right);
            cacheHeap.addNode(top);
            if (debug) {
                System.out.println("Debug: (left)removed: " + left);
                System.out.println("Debug: (right)removed: " + right);
                System.out.println("Debug: (top)added: " + top);
            }
        }
        
        // Return the root
        cacheRoot = cacheHeap.remove();
        
        // Debug
        if (debug)
            dumpHuffmanTreeCacheHeap(cacheRoot, 0);
        
        return cacheRoot;        
    }
    
    public static PairNode buildTreeUsingPairingHeap(int[] freqTable, int size) {
        // Create a pairing heap
        pairingHeap = new PairingHeap(size);
        
        // Load the data
        int i;
        for (i = 0; i < size; i++) {
            if (freqTable[i] != 0)
                pairingHeap.add(i, freqTable[i], false);
        }
        
        // Build the tree
        PairNode left;
        PairNode right;
        PairNode top;
        int freqSum;
        while (!pairingHeap.isSizeOne()) {
            left = pairingHeap.remove();
            right = pairingHeap.remove();
            freqSum = left.freq + right.freq;
            top = new PairNode(-1, freqSum, true);
            top.setLR(left, right);
            pairingHeap.addNode(top);
        }
        
        // Return the root
        pairRoot = pairingHeap.remove();
        return pairRoot;        
    }
    
    public static void dumpHuffmanTreeCacheHeap(DaryNode n, int depth) {
        String indent = "";
        int i;
        for (i = 0; i < depth; i++)
            indent += "\t";
        System.out.println(indent + "Depth: " + depth);
        System.out.println(indent + n);
        
        if (n.left != null)
            dumpHuffmanTreeCacheHeap(n.left, depth+1);
        
        if (n.right != null)
            dumpHuffmanTreeCacheHeap(n.right, depth+1);
    }
}
