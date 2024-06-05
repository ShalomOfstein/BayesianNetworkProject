import java.io.*;
//TODO: account for damaged inputs and bad networks

/**
 * This is the Main file for running the
 */
public class Ex1 {

    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        if(args.length==1){
            inputFile = args[0];
        }

        if(args.length==2){
            inputFile = args[0];
            outputFile = args[1];
        }



        // Create a new
        BayesianNetwork bn = new BayesianNetwork();




        // Read the XML file and create the Bayesian Network
        try(
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            int lineNumber =0;

            String line = br.readLine();
            if (line != null) {
                // First line: create the Bayesian Network
                String xmlFilename = line.trim();
                XmlReader.createBayesianNetwork(bn, xmlFilename);
            }
            // Second line: query the Bayesian Network
            while ((line = br.readLine()) != null) {
                if(lineNumber>0) writer.newLine();
                line = line.trim();
                if (line.isEmpty()) continue;
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
