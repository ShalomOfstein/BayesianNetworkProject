import java.util.ArrayList;

/**
 * Variable class
 * This class represents a single variable in a Bayesian Network.
 */

public class Variable {
    public String name;
    public ArrayList<Variable> parents;
    public ArrayList<Variable> children;
    public String[] outcomes;
    public double[] probabilities;

    public Variable(String name) {
        this.name = name;
        parents = new ArrayList<Variable>();
        children = new ArrayList<Variable>();
        outcomes = null;
        probabilities = null;
    }

    public void setOutcomes(String[] outcomes) {
        this.outcomes = outcomes;
        probabilities = new double[outcomes.length];
    }
    public void setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
    }

    public void addParent(Variable v) {
        parents.add(v);
        v.children.add(this);
    }
    public void removeParent(Variable v) {
        parents.remove(v);
        v.children.remove(this);
    }
    public double getProbability(String[] parentOutcomes) {
        int index = 0;

        return probabilities[index];
    }




}
