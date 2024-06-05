import java.io.BufferedWriter;
import java.io.IOException;
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
    public static void processVariableEliminationQuery(BayesianNetwork bn , String query, BufferedWriter writer) throws IOException {
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
        removeIndependentVars(bn, queryVar, evidence, vars);


        // create a list of factors from the variables
        List<Factor> factors = new ArrayList<>();
        for (Variable v : vars) {
            Factor f= new Factor(v);
            f.eliminateEvidence(evidence);
            if(f.getTable().size() > 1) {
                factors.add(f);
            }
        }
        // if there is only one factor, return the probability of the query variable
        // no need to multiply or sum
        if(factors.size()==1){
            double ans = factors.get(0).getProbability(queryVarString);
            writer.write((Math.round(ans*100000.0)/100000.0) + "," + NumOfAdditions + "," + NumOfMultiplications);
            return;
        }


        // eliminate the hidden variables
        for (Variable h : hiddenVars) {
            factors = eliminateVariable(factors, h);
        }

        // join the remaining factors and normalize the result
        Factor lastFactor = joinFactors(factors, queryVar);
        if(lastFactor!=null) {
            lastFactor.normalize();
            NumOfAdditions += lastFactor.getTable().size()-1;
        }

        // write the probability of the query variable to the output file
        double ans = factors.get(0).getProbability(queryVarString);
        writer.write((Math.round(ans*100000.0)/100000.0) + "," + NumOfAdditions + "," + NumOfMultiplications);
    }


    /**
     * This method removes the variables that are independent of the query variable given the evidence
     * @param bn the Bayesian Network
     * @param queryVar the query variable
     * @param evidence the evidence variables
     */
    public static void removeIndependentVars(BayesianNetwork bn, Variable queryVar, HashMap<String, String> evidence,List<Variable> vars ) {
        // Make a list of the variables that are independent of the query variable given the evidence
        ArrayList<Variable> varsToRemove = new ArrayList<>();
        for (Variable v : vars) {
            if (BayesBall.areIndependent(bn, queryVar.getName(), v.getName(), evidence)) {
                varsToRemove.add(v);
            }
        }

        // For each evidence Variable, if its parents are all in the varsToRemove list, remove it
        for(String EvidenceVar : evidence.keySet()) {
            Variable v = bn.getVariable(EvidenceVar);
            boolean remove = true;
            for(Variable parent : v.getParents()) {
                if(!varsToRemove.contains(parent)) {
                    remove = false;
                    break;
                }
            }
            if(remove) {
                varsToRemove.add(v);
            }
        }

        // remove the variables that are independent of the query variable given the evidence
        for (Variable v : varsToRemove) {
            vars.remove(v);
        }
    }

    /**
     * This method creates a list of the ancestors of the query variable and the evidence variables
     * This method is a recursive method that adds the ancestors of a variable to the list
     * It uses a version of DFS algorithm to traverse the Bayesian Network
     * @param bn the Bayesian Network
     * @param queryVar the query variable
     * @param evidence the evidence variables
     */

    public static void addAncestors(BayesianNetwork bn, Variable queryVar, HashMap<String, String> evidence, List<Variable> vars) {
        // add the query variable to the list
        if (!vars.contains(queryVar)) {
            vars.add(queryVar);
        }
        // get the ancestors of the query variable
        getAncestors(queryVar, vars);
        // get the ancestors of the evidence variables
        for (String e : evidence.keySet()) {
            getAncestors(bn.getVariable(e), vars);
        }
    }

    public static void getAncestors( Variable v, List<Variable> vars) {
        // get the parents of the variable
        // and get the ancestors of the parents
        for (Variable parent : v.getParents()) {
            getAncestors(parent, vars);
            if (!vars.contains(parent)) {
                vars.add(parent);
            }
        }
        // add the variable to the list
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

        // separate the factors that contain the hidden variable from the rest
        for (Factor f : factors) {
            if (f.getVariables().contains(hidden)) {
                toMultiply.add(f);
            } else {
                newFactors.add(f);
            }
        }

        // join the factors that contain the hidden variable
        Factor newFactor = joinFactors(toMultiply , hidden);

        // eliminate the hidden variable from the new factor
        if(newFactor != null) {
            int sizeOfOld = newFactor.getTable().size();
            newFactor = newFactor.EliminateVariable(hidden);
            // set the number of additions
            NumOfAdditions+= (sizeOfOld -newFactor.getTable().size());
        }
        // add the new factor to the list of factors
        if(newFactor != null && newFactor.getTable().size() > 1) newFactors.add(newFactor);

        return newFactors;
    }


    /**
     * This method joins a list of factors
     * @param factors the list of factors
     * @param hidden the hidden variable to eliminate
     * @return the product of the factors
     */

    public static Factor joinFactors(List<Factor> factors, Variable hidden) {
        if (factors.isEmpty()) return null;

        // sort the factors by size
        factors.sort(Comparator.comparingInt(f -> f.getTable().size()));
        Factor result = factors.get(0); // by default, the result is the first factor (if the list has only one factor)

        // join the factors in pairs
        while(factors.size() > 1) {
            Factor f1 = factors.remove(0);
            Factor f2 = factors.remove(0);
            result = f1.join(f2, hidden);
            insert(factors,result); // insert the new factor in the list of factors according to its size
            NumOfMultiplications += result.getTable().size(); // set the number of multiplications
        }
        return result;
    }

    /**
     * This method inserts a factor into a list of factors according to its size
     * @param factors the list of factors
     * @param f the factor to insert
     */
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
