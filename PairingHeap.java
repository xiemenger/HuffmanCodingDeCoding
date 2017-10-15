/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

/**
 * A generic Min pairing heap implementation Although we do not implement the
 * PriorityQ interface, we have implemented the all the abstract methods.
 *
 */
public class PairingHeap {

    private static final int DEFAULT_CAPACITY = 1000000;
    private PairNode root;
    private PairNode[] treeArray;
    private int heapSize;

    public PairingHeap(int size) {
        if (size == 0)
            this.treeArray = new PairNode[DEFAULT_CAPACITY];
        else
            this.treeArray = new PairNode[size];
        root = null;
        heapSize = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void makeEmpty() {
        root = null;
    }
    
    public boolean isSizeOne() {
        return heapSize == 1;
    }

    public void add(int num, int x, boolean internal) {
        PairNode newNode = new PairNode(num, x, internal);
        if (root == null) {
            root = newNode;
        } else {
            root = compareAndLink(root, newNode);
        }
        this.heapSize++;
    }
    
    public void addNode(PairNode n) {
        if (root == null) {
            root = n;
        } else {
            root = compareAndLink(root, n);
        }  
        this.heapSize++;
    }

    private PairNode compareAndLink(PairNode first, PairNode second) {
        if (second == null) {
            return first;
        }

        if (second.freq < first.freq) {
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if (first.nextSibling != null) {
                first.nextSibling.prev = first;
            }
            second.leftChild = first;
            return second;
        } else {
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null) {
                first.nextSibling.prev = first;
            }
            second.nextSibling = first.leftChild;
            if (second.nextSibling != null) {
                second.nextSibling.prev = second;
            }
            first.leftChild = second;
            return first;
        }
    }

    private PairNode combineSiblings(PairNode firstSibling) {
        if (firstSibling.nextSibling == null) {
            return firstSibling;
        }
        int numSiblings = 0;
        for (; firstSibling != null; numSiblings++) {
            treeArray = doubleIfFull(treeArray, numSiblings);
            treeArray[numSiblings] = firstSibling;
            firstSibling.prev.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull(treeArray, numSiblings);
        treeArray[numSiblings] = null;
        int i = 0;
        for (; i + 1 < numSiblings; i += 2) {
            treeArray[i] = compareAndLink(treeArray[i], treeArray[i + 1]);
        }
        int j = i - 2;
        if (j == numSiblings - 3) {
            treeArray[j] = compareAndLink(treeArray[j], treeArray[j + 2]);
        }
        for (; j >= 2; j -= 2) {
            treeArray[j - 2] = compareAndLink(treeArray[j - 2], treeArray[j]);
        }
        return treeArray[0];
    }

    private PairNode[] doubleIfFull(PairNode[] array, int index) {
        if (index == array.length) {
            // NOTE: we should never need to double the array
            System.out.println("Warning: PairingHeap array doubling");
            PairNode[] oldArray = array;
            array = new PairNode[index * 2];
            System.arraycopy(oldArray, 0, array, 0, index);
        }
        return array;
    }

    public PairNode peek() {
        if (isEmpty()) {
            return null;
        }

        return root;
    }

    public PairNode remove() {
        if (isEmpty()) {
            return null;
        }
        // Copy out the root node
        PairNode x = new PairNode(-1, -1, false);
        x.copy(root);
        if (root.leftChild == null) {
            root = null;
        } else {
            root = combineSiblings(root.leftChild);
        }
        this.heapSize--;
        return x;
    }

    public void printHeap() {
        System.out.print("\nHeap = ");
        inorder(root);
    }

    private void inorder(PairNode r) {
        if (r != null) {
            inorder(r.leftChild);
            System.out.println(r);
            inorder(r.nextSibling);
        }
    }
}
