# Bayesian Network - AI algorithms
## Description
This project is part of the AI Algorithms course in Year 2 of your university curriculum.
The project aims to implement and test various AI algorithms.
The primary focus is to process probability queries and produce corresponding outputs using the implemented algorithms.
### Bayesian Network
A Bayesian Network (BN), also known as a Belief Network or a Directed Acyclic Graphical Model (DAG), is a powerful statistical model used to represent a set of variables and their probabilistic dependencies via a directed acyclic graph. \
Here's an explanation of how Bayesian Networks efficiently represent probabilities and why they are advantageous:
#### Structure of a Bayesian Network 
* **Nodes:**
Each node represents a random variable, which can be discrete or continuous.
Edges:

* **Directed edges:** A directed edge (arrow) between nodes represent conditional dependencies. An edge from node A to node B indicates that B is conditionally dependent on A.
* **Conditional Probability Tables (CPTs):** Each node has an associated CPT that quantifies the effects of the parent nodes on the node. The CPT specifies the probability of each possible value of the node given the values of its parents.

#### Advantages of Bayesian Networks
* **Efficient representation of complex dependencies:** Bayesian Networks can efficiently represent complex dependencies between variables in a compact and intuitive manner.
* **Inference:** Bayesian Networks can be used to perform probabilistic inference, which involves calculating the probability of a query variable given observed evidence. This is useful for making predictions and decisions based on uncertain information.
* **Modularity:** Bayesian Networks are modular, meaning that they can be easily extended and modified by adding or removing nodes and edges, without rewriting the whole network. This makes them flexible and adaptable to changing requirements.
## Implemented algorithms

### Bayes Ball Algorithm ###
The Bayes Ball algorithm is a graphical method used to determine conditional independencies in Bayesian networks. It is a quick way to find out if two nodes (variables) in a Bayesian network are conditionally independent given a set of observed nodes (evidence).
### Variable Elimination ###
Variable Elimination (VE) is an exact inference algorithm for Bayesian networks. It computes marginal probabilities by systematically summing out variables from the joint distribution. VE is based on the idea of factorization, where the joint distribution is represented as a product of factors, each of which depends on a subset of variables.
## Project Structure
The project directory is organized as follows:

**scr:**
* `Ex1.java`
* `BayesBall.java`
* `BayesianNetwork.java`
* `VariableElimination.java`
* `CPT.java`
* `Factor.java`
* `FactorComperator.java`
* `Variable.java`
* `VariableElimination.java`
* `XmlReader.java`

**tests**
* `Ex1Test.java`
* *inputs*
* *outputs*

## How to Use
### Prerequisites
Ensure you have the following installed:

* JDK (Java Development Kit) 11 or higher
* Maven (for managing dependencies and running tests)
* An IDE like IntelliJ IDEA or Eclipse (optional but recommended)

To run the project with your own BN, you need the following: 

**A BN in XML format**
```xml
<NETWORK>
    <VARIABLE>
	<NAME>A</NAME>
	<OUTCOME>T</OUTCOME>
        <OUTCOME>F</OUTCOME>
    </VARIABLE>
    .
    .
    .
    <DEFINITION>
        <FOR>A</FOR>
        <GIVEN>E</GIVEN>
        <GIVEN>B</GIVEN>
        <TABLE>0.95 0.05 0.29 0.71 0.94 0.06</TABLE>
    </DEFINITION>
</NETWORK>
``` 
**A text file containing the XML file and queries you want to answer:**
```txt
Bayesian_net.xml
B-E|
B-E|J=T
P(B=T|J=T,M=T) A-E
P(J=T|B=T) A-E-M
```
### Format of the queries:
**BayesBall Query:** A-B|E1=e1,E2=e2,...,Ek=ek \
The query is asking if A is independent of B given the evidence E1=e1,E2=e2,...,Ek=ek.

**Variable Elimination Query:** P(A=a|E1=e1,E2=e2,...,Ek=ek) H1-H2-...-Hn \
The query is asking for the probability of A=a given the evidence E1=e1,E2=e2,...,Ek=ek \
the hidden variables are H1,H2,...,Hn in the order to be eliminated.

### Setup
Clone the repository:
```bash
git clone https://github.com/ShalomOfstein/BayesianNetworkProject.git
cd EX1
```
Open the project in your IDE:

* If using IntelliJ IDEA, choose `File > Open` and select the project directory.
* If using Eclipse, choose `File > Import > Existing Maven Projects` and select the project directory.

### Running the project
To run the project, you need to run the `Ex1.java` file. The program will read the input file and answer the queries using the implemented algorithms.

Ex1 can take your own input text file as an argument:
```bash
java Ex1 my_input_file.txt
```
If no argument is provided, the program will use the default input file `input.txt`.

It will generate an output file with the answers to the queries in the same directory as the Ex1.java file. or you can provide an output file as an argument:
```bash
java Ex1 my_input_file.txt my_output_file.txt
```

### Running the tests
To run the tests, you need to run the `Ex1Test.java` file. The tests will read the input files from the `inputs` directory and compare the output with the expected output in the `outputs` directory.

## Contributions
Contributions to this project are welcome. If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.