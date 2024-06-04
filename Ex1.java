import java.io.*;
//TODO: account for damaged inputs and bad networks

public class Ex1 {
    static boolean DEBUG = false;

    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        if(args.length==2){
            inputFile = args[0];
            outputFile = args[1];
        }

        if(DEBUG ){
            inputFile = "input.txt";
            outputFile = "outputs/Actual/outputFile.txt";
        }



        BayesianNetwork bn = new BayesianNetwork();

        PrintStream originalOut = System.out;
        try (PrintStream out = new PrintStream(new FileOutputStream(outputFile))) {
            System.setOut(out);

            // Read the XML file and create the Bayesian Network
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String line = br.readLine();
                if (line != null) {
                    // First line: create the Bayesian Network
                    String xmlFilename = line.trim();
                    XmlReader.createBayesianNetwork(bn, xmlFilename);
                }

                // Second line: query the Bayesian Network
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    if (line.startsWith("P(")) {
                        VariableElimination.processVariableEliminationQuery(bn, line);

                    } else {
                        boolean ans = BayesBall.processBayesBallQuery(bn, line);
                        System.out.println(ans ? "yes" : "no");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }
    }
}
