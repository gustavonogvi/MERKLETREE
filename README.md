# Merkle Tree Project in Java

This project implements a Merkle Tree in Java, using the Tiger hash algorithm provided by the Bouncy Castle library. The Merkle Tree is a data structure used to verify the integrity of a set of data.

## Requirements

- Bouncy Castle library
## Setup
1. Download and install the [Bouncy Castle library](https://www.bouncycastle.org/download/bouncy-castle-java/#latest).
2. Add the library to your Java project.

## Project Structure
- `Node`: Represents a node in the Merkle Tree.
- `MerkleTree`: Constructs the Merkle Tree from a list of values.
- `Main`: Main class that reads data from a file, constructs the Merkle Tree, and verifies the data integrity.

## Classes and Methods

### Node
This class represents a node in the Merkle Tree. Each node contains references to its left and right children, its hash value, its content, and a flag indicating if it was copied.

### MerkleTree
This class constructs the Merkle Tree from a list of values. It includes methods to build the tree, print the tree structure, and retrieve the root hash.

### Main
This class is responsible for reading data from a file, constructing the Merkle Tree, and verifying the data integrity. It also handles saving and loading the root hash to and from a file.

## How to Use
1. Ensure the Bouncy Castle library is added to your project.
2. Place the input data file (`dataHash.txt`) in the specified path.
3. Run the `Main` class. The program will read the input data, construct the Merkle Tree, and print the tree structure.
4. The program will compare the new root hash with the original root hash stored in `rootHash.txt`. If the hashes match, the data integrity is verified. If not, the new root hash will be saved to the file.

## Example
1. Prepare an input file (`dataHash.txt`) with data values.
2. Run the program:
   - The program reads the input values.
   - Constructs the Merkle Tree.
   - Prints the tree structure.
   - Verifies the integrity of the data against the saved root hash.
   - Saves the new root hash if it differs from the original.

## Conclusion
This project demonstrates the implementation of a Merkle Tree in Java using the Tiger hash algorithm. It showcases how to construct the tree, print its structure, and verify data integrity.
