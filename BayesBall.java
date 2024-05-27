import java.util.*;

public class BayesBall {

    /**
     * This is the main method to process the BayesBall query
     * it will return true if the variables are independent with respect to the evidence
     * otherwise it will return false
     * @param network the Bayesian Network
     * @param line the query
     * @return true if the variables are independent, false otherwise
     */
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

        // check if the variables are independent with respect to the evidence
        boolean ans =  areIndependent(network, var1, var2, evidence);

        // reset the evidence so the next query can be processed
        resetEvidence(network);

        return ans;
    }

    /**
     * This method checks if two variables are independent with respect to the evidence
     *
     * @param network the Bayesian Network
     * @param var1 the first variable name
     * @param var2 the second variable name
     * @param evidence a hash map of the evidence variables name (String) and their values (String)
     * @return
     */
    private static boolean areIndependent(BayesianNetwork network, String var1, String var2, HashMap<String, String> evidence) {

        // get the variable objects
        Variable v1 = network.getVariable(var1);
        Variable v2 = network.getVariable(var2);

        // a set the variables that belong to the evidence as marked
        setEvidenvce(network, evidence);

        // Perform DFS to check reachability
        boolean search = search(v1, v2, null);
        return !search;
    }

    /**
     * This method performs a Depth First Search to check if two variables are independent
     * it utilizes the BayesBall algorithm and reachability rules as learned in class
     *
     * Say we reach a node X (called current) from a node Y (called comingFrom)
     * we divide into 4 cases:
     * 1. If X is NOT evidence, and we came from a parent
     *     - we can go to any child of X
     * 2. If X is NOT evidence, and we came from a child
     *    - we can go to any parent of X
     *    - we can go to any child of X (including the one we came from)
     * 3. If X is evidence, and we came from a parent
     *   - we can go to any parent of X (including the one we came from)
     * 4. If X is evidence, and we came from a child
     *   - we cannot go to any parent or child of X (including the one we came from)
     *
     *
     * To avoid infinite loops, we mark the variables as observed when we visit them
     * if we reach a variable that has been observed twice, we return false
     *
     * @param current the current variable
     * @param end the end variable we are trying to reach
     * @param comingFrom the variable that we came from (parent or child)
     * @return true if the variables are reachable, false otherwise
     */
    private static boolean search(Variable current, Variable end, Variable comingFrom) {

        // Base Case: if we reach the end variable return true
        if (current == end) {
            return true;
        }

        // if we have already observed this variable twice, return false
        if(current.Observed()==2){
            return false;
        }

        // mark the variable as observed
        current.setObserved(current.Observed()+1);

        // Breakdown into 4 cases:
        //
        // if current is NOT evidence
        if (!current.isEvidence()) {
            // Case 1:  if we came from a parent (current is NOT evidence)
            if(current.getParents().contains(comingFrom)||comingFrom==null) {
                for (Variable child : current.getChildren()) {
                    if (search(child, end, current)) {
                        return true;
                    }
                }
                //  Case 2: if we came from a child (current is NOT evidence)
            } else {
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
        }

    }


}
