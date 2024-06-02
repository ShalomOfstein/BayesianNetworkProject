import java.io.BufferedReader;
import java.io.FileReader;
//TODO: account for damaged inputs and bad networks

public class Ex1 {
    static boolean DEBUG = true;

    public static void main(String[] args) {
        if (!DEBUG && args.length < 1) {
            System.out.println("Usage: java BayesianNetworkMain <inputfile.txt>");
            return;
        }
        if(DEBUG) {
            args = new String[1];
            args[0] = "input1.txt";
        }

        String inputFile = args[0];
        BayesianNetwork bn = new BayesianNetwork();

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

                }else {
                    boolean ans = BayesBall.processBayesBallQuery(bn, line);
                    System.out.println(ans?"yes":"no");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
