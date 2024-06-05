import java.util.HashMap;

/**
 * BayesianNetwork.java
 * This class represents a Bayesian Network. It contains a list of variables
 * that are part of the network.
 */

public class BayesianNetwork {

    // A list of variables in the network
    public HashMap<String, Variable> variables;

    /**
     * Constructor
     */
    public BayesianNetwork() {
        variables = new HashMap<String, Variable>();
    }

    /**
     * This method returns a Hash Map of variables in the network <variable name, variable>
     * @return a Hash Map of variables in the network
     */
    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    /**
     * This method adds a variable to the network
     * @param v the variable to add
     */
    public void addVariable(Variable v) {
        variables.put(v.getName(), v);
    }

    /**
     * This method returns a variable given its name
     * @param name the name of the variable
     * @return the variable
     */
    public Variable getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        throw new RuntimeException("Variable not found: " + name);
    }

    /**
     * This method checks if a variable is in the network given its name
     * @param name the name of the variable
     * @return true if the variable is in the network, false otherwise
     */
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
}
