/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

/**
 *
 */
public class PairNode {
    public int num;
    public int freq;
    public PairNode leftChild;
    public PairNode nextSibling;
    public PairNode prev;
    public boolean internal; // used by Huffman tree
    public PairNode left;    // used by the huffman tree
    public PairNode right;   // used by the huffman tree

    /* Constructor */
    public PairNode(int num, int x, boolean internal) {
        this.num = num;
        freq = x;
        leftChild = null;
        nextSibling = null;
        prev = null;
        this.internal = internal;
        this.left = null;
        this.right = null;
    }
    
    public void copy(PairNode n) {
        this.num = n.num;
        this.freq = n.freq;
        this.leftChild = n.leftChild;
        this.nextSibling = n.nextSibling;
        this.prev = n.prev;
        this.internal = n.internal;
        this.left = n.left;
        this.right = n.right;
    }
    
    public void setLR(PairNode l, PairNode r) {
        this.left = l;
        this.right = r;
    }
    
    public boolean isLeaf() {
        return ((this.left == null) &&
                (this.right == null) &&
                (this.num != -1) &&
                (this.internal == false));
    }
    
    @Override
    public String toString() {
        return ("PairNode: num=" + this.num + ", freq=" + this.freq + ", internal=" + this.internal);
    }
}
