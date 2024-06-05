import java.io.*;
//TODO: account for damaged tests.inputs and bad networks

/**
 * This is the Main file for running the Bayesian Network queries.
 * The program reads the input file and processes the queries.
 * The structure of the input file is as follows:
 *      1. The first line contains the name of the XML file that contains the Bayesian Network.
 *      2. The following lines contain the queries to be processed.
 * The queries can be of two types:
 *  1. A-B|E1=e1,E2=e2,…,Ek=ek (Bayes Ball Query)
 *      Are A and B independent given E1=e1,E2=e2,…,Ek=ek?
 *  2. P(Q=q|E1=e1, E2=e2, …, Ek=ek) H1-H2-…-Hj (Variable Elimination Query)
 *      What is the probability of Q=q given E1=e1, E2=e2, …, Ek=ek?
 *      H1, H2, …, Hj are the order of elimination of the hidden variables.
 * The output is written to the output file.
 */
public class Ex1 {

    /**
     * Main method for running the Bayesian Network queries.
     * @param args The input and output file names.
     * if no arguments are provided, the default input file is "input.txt" and the default output file is "output.txt".
     *
     */
    public static void main(String[] args) {

        // Default input and output file names
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        // Read the input and output file names from the command line arguments
        if(args.length==1){
            inputFile = args[0];
        }
        // Read the input and output file names from the command line arguments
        if(args.length==2){
            inputFile = args[0];
            outputFile = args[1];
        }

        // Create a new Bayesian Network
        BayesianNetwork bn = new BayesianNetwork();

        // Read the input file and process the queries to write the output to the output file
        try(
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){

            int lineNumber =0;

            String line = br.readLine();
            if (line != null) {

                // First line: create the Bayesian Network
                String xmlFilename = line.trim();

                // Read the XML file and create the Bayesian Network
                XmlReader.createBayesianNetwork(bn, xmlFilename);
            }
            // Second line: query the Bayesian Network
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // enter a new line for each query

                if(lineNumber>0) writer.newLine();
                // Process the query
                if (line.startsWith("P(")) {
                    VariableElimination.processVariableEliminationQuery(bn, line, writer);
                } else {
                    if(BayesBall.processBayesBallQuery(bn, line)){
                        writer.write("yes");
                    }else{
                        writer.write("no");
                    }
                }
                lineNumber++;
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

    }
}
