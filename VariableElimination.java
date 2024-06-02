import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * VariableElimination.java
 * This class implements the Variable Elimination algorithm for a Bayesian Network
 * */


public class VariableElimination {

    public static int NumOfAdditions;
    public static int NumOfMultiplications;


    /**
     * This method processes a query using the Variable Elimination algorithm
     * the query is of the form P(Q=q|E1=e1, E2=e2, …, Ek=ek) H1-H2-…-Hj
     * where Q is the query variable,
     * E1, E2, …, Ek are the evidence variables,
     * q, e1, e2, …, ek are the values of the query and evidence variables,
     * and H1, H2, …, Hj are the hidden variables. in the order they should be eliminated
     *
     * @param bn the Bayesian Network
     * @param query the query
     */
    public static void processVariableEliminationQuery(BayesianNetwork bn , String query){
        NumOfAdditions = 0;
        NumOfMultiplications = 0;

        // parse the query int the different parts
        String[] queryParts = query.split("\\|");
        String queryVerString = queryParts[0].substring(2);
        String[] evidenceAndHidden = queryParts[1].split("\\)");
        String evidenceString = evidenceAndHidden.length > 0 ? evidenceAndHidden[0] : "";
        String hiddenString = evidenceAndHidden.length > 1 ? evidenceAndHidden[1] : null;



        // create a list of the hidden variables in the same order they should be eliminated
        List<Variable> hiddenVars = new ArrayList<>();
        if(hiddenString != null) {
            String[] hidden = hiddenString.split("-");
            for (String h : hidden) {
                hiddenVars.add(bn.getVariable(h));
            }
        }

        // get a map of the evidence variables and their values
        HashMap<String, String> evidence = BayesBall.parseEvidence(evidenceString);

        // get the query variable
        String varName = queryVerString.split("=")[0];

        Variable queryVar = bn.getVariable(varName);
        // create a list of the variables in the network to make into factors
        List<Variable> vars = new ArrayList<>();

        // add only the relevant variables to the list
        vars = addAncestors(bn, queryVar, evidence, vars);
        //addIndependentVars(bn, queryVar, evidence, vars);

        // create a list of factors from the variables
        List<Factor> factors = new ArrayList<>();
        for (Variable v : vars) {
            factors.add(new Factor(v, evidence));
        }




    }


    /**
     * This method creates a list of the variables in the network that are independent of the query given the evidence
     * @param bn the Bayesian Network
     * @param queryVar the query variable
     * @param evidence the evidence variables
     * @return a list of the variables that are independent of the query given the evidence
     */
    public static void addIndependentVars(BayesianNetwork bn, Variable queryVar, HashMap<String, String> evidence,List<Variable> vars ) {
        for (Variable v : bn.getVariables().values()) {
            if (!BayesBall.areIndependent(bn, queryVar.getName(), v.getName(), evidence)) {
                vars.add(v);
            }
        }
    }

    /**
     * This method creates a list of the ancestors of the query variable and the evidence variables
     * @param bn the Bayesian Network
     * @param queryVar the query variable
     * @param evidence the evidence variables
     * @return a list of the ancestors of the query variable and the evidence variables
     */

    public static List<Variable> addAncestors(BayesianNetwork bn, Variable queryVar, HashMap<String, String> evidence, List<Variable> vars) {
        if (!vars.contains(queryVar)) {
            vars.add(queryVar);
        }
        getAncestors(bn, queryVar, vars);
        for (String e : evidence.keySet()) {
            getAncestors(bn, bn.getVariable(e), vars);
        }
        return vars;
    }

    public static void getAncestors(BayesianNetwork bn, Variable v, List<Variable> vars) {
        for (Variable parent : v.getParents()) {
            getAncestors(bn, parent, vars);
            if (!vars.contains(parent)) {
                vars.add(parent);
            }
        }
        if (!vars.contains(v)) {
            vars.add(v);
        }
    }



}
