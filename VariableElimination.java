import java.util.*;

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
        String queryVarString = queryParts[0].substring(2);
        String[] evidenceAndHidden = queryParts[1].split("\\)");
        String evidenceString = evidenceAndHidden.length > 0 ? evidenceAndHidden[0] : "";
        String hiddenString = evidenceAndHidden.length > 1 ? evidenceAndHidden[1] : null;



        // create a list of the hidden variables in the same order they should be eliminated
        List<Variable> hiddenVars = new ArrayList<>();
        if(hiddenString != null) {
            String[] hidden = hiddenString.substring(1).split("-");
            for (String h : hidden) {
                hiddenVars.add(bn.getVariable(h));
            }
        }

        // get a map of the evidence variables and their values
        HashMap<String, String> evidence = BayesBall.parseEvidence(evidenceString);

        // get the query variable
        String varName = queryVarString.split("=")[0];
        Variable queryVar = bn.getVariable(varName);

        // create a list of the variables in the network to make into factors
        List<Variable> vars = new ArrayList<>();

        // add only the relevant variables to the list
        addAncestors(bn, queryVar, evidence, vars);
        addIndependentVars(bn, queryVar, evidence, vars);


        // create a list of factors from the variables
        List<Factor> factors = new ArrayList<>();
        for (Variable v : vars) {
            Factor f= new Factor(v);
            f.eliminateEvidence(evidence);
            if(f.getTable().size() > 1) {
                factors.add(f);
            }
        }

        if(factors.size()==1){
            double ans = factors.getFirst().getProbability(queryVarString);
            System.out.println(Math.round(ans*100000.0)/100000.0);
        }

        // eliminate the hidden variables

        for (Variable h : hiddenVars) {
            factors = eliminateVariable(factors, h);
        }
        if(factors.size()==1){
            double ans = factors.getFirst().getProbability(queryVarString);
            System.out.println(Math.round(ans*100000.0)/100000.0);
            return;
        }

    }


    /**
     * This method creates a list of the variables in the network that are independent of the query given the evidence
     * @param bn the Bayesian Network
     * @param queryVar the query variable
     * @param evidence the evidence variables
     */
    public static void addIndependentVars(BayesianNetwork bn, Variable queryVar, HashMap<String, String> evidence,List<Variable> vars ) {
        ArrayList<Variable> varsToRemove = new ArrayList<>();
        for (Variable v : vars) {
            if (BayesBall.areIndependent(bn, queryVar.getName(), v.getName(), evidence)) {
                varsToRemove.add(v);
            }
        }
        for(Variable v : vars) {
            for(int i= 0 ; i< varsToRemove.size(); i++) {
                if(v.getParents().contains(varsToRemove.get(i))) {
                    varsToRemove.add(v);
                }
            }
        }
        for (Variable v : varsToRemove) {
            vars.remove(v);
        }
    }

    /**
     * This method creates a list of the ancestors of the query variable and the evidence variables
     * @param bn the Bayesian Network
     * @param queryVar the query variable
     * @param evidence the evidence variables
     */

    public static void addAncestors(BayesianNetwork bn, Variable queryVar, HashMap<String, String> evidence, List<Variable> vars) {
        if (!vars.contains(queryVar)) {
            vars.add(queryVar);
        }
        getAncestors(bn, queryVar, vars);
        for (String e : evidence.keySet()) {
            getAncestors(bn, bn.getVariable(e), vars);
        }
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

    /**
     * This method eliminates a variable from a list of factors
     * @param factors the list of factors
     * @param hidden the hidden variable to eliminate
     * @return a new list of factors with the variable eliminated
     */

    public static List<Factor> eliminateVariable(List<Factor> factors, Variable hidden) {
        List<Factor> newFactors = new ArrayList<>();
        List<Factor> toMultiply = new ArrayList<>();
        for (Factor f : factors) {
            if (f.getVariables().contains(hidden)) {
                toMultiply.add(f);
            } else {
                newFactors.add(f);
            }
        }
        Factor newFactor = multiplyFactors(toMultiply , hidden);
        if(newFactor != null) newFactor = newFactor.EliminateVariable(hidden);
        if(newFactor != null && newFactor.getTable().size() > 1) newFactors.add(newFactor);

        return newFactors;
    }


    /**
     * This method multiplies a list of factors
     * @param factors the list of factors
     * @param hidden the hidden variable to eliminate
     * @return the product of the factors
     */

    public static Factor multiplyFactors(List<Factor> factors, Variable hidden) {
        if (factors.isEmpty()) return null;

        Factor result = factors.getFirst();

        while(factors.size() > 1) {
            Factor f1 = factors.removeFirst();
            Factor f2 = factors.removeFirst();
            result = f1.join(f2, hidden);
            insert(factors,result);

        }
        return result;
    }

    public static void insert(List<Factor> factors, Factor f) {
        if(factors.isEmpty()) {
            factors.add(f);
            return;
        }
        for(int i = 0; i < factors.size(); i++) {
            if(factors.get(i).getTable().size() > f.getTable().size()) {
                factors.add(i,f);
                return;
            }
        }
    }




}
