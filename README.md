
# Merkle Tree Implementation

## Overview

This Java program demonstrates the implementation of a Merkle tree, a data structure used in cryptographic applications for efficiently summarizing and verifying the integrity of large sets of data. The Merkle tree is constructed using a hash function, creating a tree structure where each leaf node represents a data element and each non-leaf node represents the hash of its children's hashes.

## Features

- **Merkle Tree Construction**: The program constructs a Merkle tree from a list of input values.
- **Hash Function**: It utilizes the Tiger hash function for generating hash values.
- **Tree Printing**: The constructed Merkle tree can be printed to visualize its structure.
- **Root Hash Retrieval**: The root hash of the Merkle tree, representing the overall integrity of the data set, can be obtained.

## Dependencies

- **Bouncy Castle Provider**: The Bouncy Castle cryptographic library is used for security purposes. [Download Here](https://www.bouncycastle.org/download/bouncy-castle-java/)
- **Java I/O**: Standard Java I/O libraries are used for file handling.
- **Java Security**: Java Security libraries are employed for cryptographic operations.

## Usage

1. **Input Data**: Provide input data in a text file. Each line of the file represents a data element.
2. **Compile**: Compile the Java source code using a Java compiler.
3. **Run**: Execute the compiled program, providing the path to the input data file as a command-line argument.
4. **Output**: The program will print the input data, construct the Merkle tree, print its structure, and display the root hash.

## Example

Suppose you have a file named `dataHash.txt` containing the following data:
```
data1
data2
data3
data4
```

You can run the program as follows:
```
java Main
```

The program will output the constructed Merkle tree and the root hash based on the provided data.

## Note

- Ensure that the Bouncy Castle Provider library is correctly included in the classpath for cryptographic operations.

---

This should provide users with a comprehensive overview of your program's functionality and dependencies.
