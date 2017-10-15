/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package huffmanperf;

import java.util.NoSuchElementException;

/**
 * A min d-ary heap implementation
 * with PriorityQ abstract methods implemented
 * NOTE:
 * To create a binary heap, call DaryHeap(2);
 * To create a 4-way heap, call DaryHeap(4);
 */
public class DaryHeap {
    private static final int DEFAULT_CAPACITY = 1000000;
    private int d;
    private int heapSize;
    private DaryNode[] heap;

    public DaryHeap(int size, int numChild) {
        int initSize;
        heapSize = 0;
        d = numChild;
        if (size == 0)
            initSize = DEFAULT_CAPACITY + 1;
        else
            initSize = size + 1;
        heap = new DaryNode[initSize];
        //Arrays.fill(heap, -1);
        int i;
        for (i = 0; i < initSize; i++)
            heap[i] = new DaryNode(-1, -1, false);
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public boolean isFull() {
        return heapSize == heap.length;
    }
    
    public boolean isSizeOne() {
        return heapSize == 1;
    }

    public void clear() {
        heapSize = 0;
    }

    private int parent(int i) {
        return (i - 1) / d;
    }

    private int kthChild(int i, int k) {
        return d * i + k;
    }

    public void add(int num, int x, boolean internal) {
        if (isFull()) {
            throw new NoSuchElementException("Overflow Exception");
        }
        //heap[heapSize++] = freq;
        heap[heapSize].num = num;
        heap[heapSize].freq = x;
        heap[heapSize++].internal = internal;
        heapifyUp(heapSize - 1);
    }
    
    public void addNode(DaryNode n) {
        if (isFull()) {
            throw new NoSuchElementException("Overflow Exception");
        }
        heap[heapSize++].copy(n);
        heapifyUp(heapSize - 1);        
    }

    public DaryNode findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Underflow Exception");
        }
        return heap[0];
    }

    public DaryNode delete(int ind) {
        if (isEmpty()) {
            throw new NoSuchElementException("Underflow Exception");
        }
        //DaryNode keyItem = heap[ind];
        DaryNode keyItem = new DaryNode(-1, -1, false);
        keyItem.copy(heap[ind]);
        //heap[ind] = heap[heapSize - 1];
        heap[ind].copy(heap[heapSize - 1]);
        heapSize--;
        heapifyDown(ind);
        return keyItem;
    }
    
    public DaryNode remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Underflow Exception");
        }
        return this.delete(0);
    }

    private void heapifyUp(int childInd) {
        int tmp = heap[childInd].freq;
        //int tmpN = heap[childInd].num;
        //boolean tmpI = heap[childInd].internal;
        DaryNode tmpNode = new DaryNode(-1, -1, false);
        tmpNode.copy(heap[childInd]);
        while (childInd > 0 && tmp < heap[parent(childInd)].freq) {
            //heap[childInd] = heap[parent(childInd)];
            heap[childInd].copy(heap[parent(childInd)]);
            childInd = parent(childInd);
        }
        //heap[childInd].freq = tmp;
        //heap[childInd].num = tmpN;
        //heap[childInd].internal = tmpI;
        heap[childInd].copy(tmpNode);
    }

    private void heapifyDown(int ind) {
        int child;
        int tmp = heap[ind].freq;
        //int tmpN = heap[ind].num;
        //boolean tmpI = heap[ind].internal;
        DaryNode tmpNode = new DaryNode(-1, -1, false);
        tmpNode.copy(heap[ind]);
        while (kthChild(ind, 1) < heapSize) {
            child = minChild(ind);
            if (heap[child].freq < tmp) {
                //heap[ind] = heap[child];
                heap[ind].copy(heap[child]);
            } else {
                break;
            }
            ind = child;
        }
        //heap[ind].freq = tmp;
        //heap[ind].num = tmpN;
        //heap[ind].internal = tmpI;
        heap[ind].copy(tmpNode);
    }

    private int minChild(int ind) {
        int bestChild = kthChild(ind, 1);
        int k = 2;
        int pos = kthChild(ind, k);
        while ((k <= d) && (pos < heapSize)) {
            if (heap[pos].freq < heap[bestChild].freq) {
                bestChild = pos;
            }
            k++;
            pos = kthChild(ind, k);
        }
        return bestChild;
    }

    public void printHeap() {
        System.out.print("\nHeap = ");
        for (int i = 0; i < heapSize; i++) {
            System.out.println(heap[i]);
        }
        System.out.println();
    }
}
