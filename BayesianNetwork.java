import java.util.ArrayList;

/**
 * BayesianNetwork.java
 * This class represents a Bayesian Network. It contains a list of variables
 * that are part of the network.
 */

public class BayesianNetwork {
    public ArrayList<Variable> variables;

    public BayesianNetwork() {
        variables = new ArrayList<Variable>();
    }

    public void addVariable(Variable v) {
        variables.add(v);
    }

    public Variable getVariable(String name) {
        for (Variable v : variables) {
            if (v.name.equals(name)) {
                return v;
            }
        }
        return null;
    }


}
