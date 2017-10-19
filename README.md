# HuffmanCodingDeCoding
Use Huffman Coding to reduce the data amount and Decoding to resume the data file.


High-level:
HuffmanPerf is the main class for the performance analysis using binary heap, 4-way heap, and pairing heap.
Encoder is the main class for the Huffman encoding, with the real implementation in HuffmanEncoder class.
Decoder is the main class for the Huffman decoding, with the real implementation in HuffmanDecoder class.

DaryHeap is the implementation of d-ary heap, supporting both the binary heap (2-ary heap) and the 4-way heap, where the element is implemented by DaryNode class. PairingHeap is the implementation of the pairing heap, where the element is
implemented by PairingNode.

HuffmanTree is the implementation of building Huffman trees using different heaps. HuffmanNode is currently not used, but for future reference, if we want to unify the node structures used by d-ary heap and the pairing heap.
