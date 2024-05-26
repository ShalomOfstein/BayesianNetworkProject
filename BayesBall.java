import java.util.*;

public class BayesBall {
    static int count = 0;
    public static boolean processBayesBallQuery(BayesianNetwork network, String line) {
        // Split the query into variables and evidence
        String[] parts = line.split("\\|");
        String[] variables = parts[0].split("-");
        String given = parts.length > 1 ? parts[1] : "";

        // get a map of the evidence variables and their values
        HashMap<String, String> evidence = parseEvidence(given);

        // get the variables
        String var1 = variables[0];
        String var2 = variables[1];

        // check if the variables are independent
        boolean ans =  areIndependent(network, var1, var2, evidence);
        resetEvidence(network);
        return ans;
    }


    private static boolean areIndependent(BayesianNetwork network, String var1, String var2, HashMap<String, String> evidence) {
        // get the variable objects
        Variable v1 = network.getVariable(var1);
        Variable v2 = network.getVariable(var2);
        setEvidenvce(network, evidence);

        // Perform DFS to check reachability
        boolean search = search(v1, v2, null);
        return !search;
    }

    private static boolean search(Variable current, Variable end, Variable comingFrom) {
        if (current == end) {
            return true;
        }




        // if X is NOT evidence
        if (!current.isEvidence()) {
            if(current.getParents().contains(comingFrom)||comingFrom==null) { // if we came from a parent
                for (Variable child : current.getChildren()) {
                    if (search(child, end, current)) {
                        return true;
                    }
                }
            } else { // if we came from a child
                for (Variable parent : current.getParents()) {
                    if (search(parent, end, current)) {
                        return true;
                    }
                }
                for (Variable child : current.getChildren()) {
                    if (search(child, end, current)) {
                        return true;
                    }
                }
            }
        } else { // if X is evidence
            if(current.getParents().contains(comingFrom)||comingFrom==null) { // if we came from a parent
                for (Variable parent : current.getParents()) {
                    if (search(parent, end, current)) {
                        return true;
                    }
                }
            }else { // if we came from a child
                return false;
            }


        }
        return false;
    }




    private static HashMap<String, String> parseEvidence(String given) {
        HashMap<String, String> evidence = new HashMap<>();
        if (!given.isEmpty()) {
            String[] evidenceParts = given.split(",");
            for (String evidencePart : evidenceParts) {
                String[] keyValue = evidencePart.split("=");
                evidence.put(keyValue[0], keyValue[1]);
            }
        }
        return evidence;
    }

    private static void setEvidenvce(BayesianNetwork network, HashMap<String, String> evidence) {
        for (Map.Entry<String, String> entry : evidence.entrySet()) {
            String varName = entry.getKey();
            Variable v = network.getVariable(varName);
            v.setEvidence(true);
        }
    }

    private static void resetEvidence(BayesianNetwork network) {
        for (Variable v : network.getVariables().values()) {
            v.setEvidence(false);
            v.setObserved(0);
            count= 0;
        }

    }


}
