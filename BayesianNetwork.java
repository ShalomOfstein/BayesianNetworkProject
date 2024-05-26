
import java.util.HashMap;

/**
 * BayesianNetwork.java
 * This class represents a Bayesian Network. It contains a list of variables
 * that are part of the network.
 */

public class BayesianNetwork {
    public HashMap<String, Variable> variables;

    public BayesianNetwork() {
        variables = new HashMap<String, Variable>();
    }

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public void addVariable(Variable v) {
        variables.put(v.getName(), v);
    }

    public Variable getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        throw new RuntimeException("Variable not found: " + name);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }



}
