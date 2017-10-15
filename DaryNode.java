/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

/**
 *
 */
public class DaryNode {
    public int num;   // number
    public int freq;     // freq
    public boolean internal; // interanl - used by the huffman tree encoding
    public DaryNode left;    // used by the huffman tree
    public DaryNode right;   // used by the huffman tree

    public DaryNode(int num, int x, boolean internal) {
        this.num = num;
        this.freq = x;
        this.internal = internal;
        this.left = null;
        this.right = null;
    }
    
    public void copy(DaryNode n) {
        this.num = n.num;
        this.freq = n.freq;
        this.internal = n.internal;
        this.left = n.left;
        this.right = n.right;
    }
    
    public void setLR(DaryNode l, DaryNode r) {
        this.left = l;
        this.right = r;
    }
    
    public void setFreq(int freq) {
        this.freq = freq;
    }
    
    public boolean isLeaf() {
        return ((this.left == null) &&
                (this.right == null) &&
                (this.num != -1) &&
                (this.internal == false));
    }
    
    @Override
    public String toString() {
        return ("DaryNode: num=" + this.num + ", freq=" + this.freq + ", internal=" + this.internal);
                // + ", left=[" + this.left + "], right=[" + this.right + "]");
    }
}
