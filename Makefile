# The makefile
CC=javac

.PHONY: all
all: HuffmanPerf encoder decoder

HuffmanPerf: HuffmanPerf.java
	$(CC) HuffmanPerf.java

encoder: encoder.java
	$(CC) encoder.java

decoder: decoder.java
	$(CC) decoder.java

clean:
	rm -rf *.class
