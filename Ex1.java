import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Ex1 {
    static boolean DEBUG = true;

    public static void main(String[] args) {
        if (!DEBUG && args.length < 1) {
            System.out.println("Usage: java BayesianNetworkMain <inputfile.txt>");
            return;
        }
        if(DEBUG) {
            args = new String[1];
            args[0] = "input.txt";
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
                    processVariableEliminationQuery(bn, line);
                }else {
                    boolean ans = BayesBall.processBayesBallQuery(bn, line);
                    System.out.println(ans?"yes":"no");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processVariableEliminationQuery(BayesianNetwork bn, String query) {
        System.out.println("Variable Elimination Query: " + query);
//        String[] parts = query.split("\\|");
//        String[] variables = parts[0].substring(2, parts[0].length() - 1).split(",");
//        String evidence = parts.length > 1 ? parts[1] : "";
//        String[] given = evidence.split(",");
//        Map<String, String> evidence = parseEvidence(given);
//
//        String queryVariable = variables[0];
//        String[] hiddenVariables = Arrays.copyOfRange(variables, 1, variables.length);
//
//        double probability = bn.variableElimination(queryVariable, hiddenVariables, evidence);
//        System.out.println(probability);

    }
}
