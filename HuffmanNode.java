/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

/**
 * NOTE: It is NOT used for now...
 * but good to have for a reference...
 */
public class HuffmanNode {
    int num;
    int freq;
    int edge;
    HuffmanNode left;
    HuffmanNode right;
    HuffmanNode parent;
    boolean internal;
    
    public HuffmanNode(int num, int freq, int edge, HuffmanNode left, HuffmanNode right, HuffmanNode parent, boolean internal) {
        this.num = num;
        this.freq = freq;
        this.edge = edge;
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.internal = internal;
    }
    
    public boolean isRoot(HuffmanNode n) {
        return (n.parent == null);
    }
    
    public boolean isInternal(HuffmanNode n) {
        return n.internal;
    }
    
    public void setLeft(HuffmanNode l) {
        this.left = l;
    }
    
    public void setRight(HuffmanNode r) {
        this.right = r;
    }
    
    public void setParent(HuffmanNode p) {
        this.parent = p;
    }
    
    public void dumpNode() {
        System.out.println("HuffmanNode=" + this +
                ", num=" + this.num +
                ", freq=" + this.freq +
                ", edge=" + this.edge +
                ", left=" + this.left + 
                ", right=" + this.right +
                ", parent=" + this.parent +
                ", internal=" + this.internal);
    }
    
}
