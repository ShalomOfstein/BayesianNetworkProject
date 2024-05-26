import java.util.ArrayList;

/**
 * Variable class
 * This class represents a single variable in a Bayesian Network.
 */

public class Variable {
    private String name;
    private ArrayList<Variable> parents;
    private ArrayList<Variable> children;
    private String[] outcomes;
    private double[] probabilities;

    public Variable(String name) {
        this.name = name;
        parents = new ArrayList<Variable>();
        children = new ArrayList<Variable>();
        outcomes = null;
        probabilities = null;
    }

    public String getName() {
        return name;
    }
    public ArrayList<Variable> getParents() {
        return parents;
    }
    public ArrayList<Variable> getChildren() {
        return children;
    }
    public String[] getOutcomes() {
        return outcomes;
    }
    public double[] getProbabilities() {
        return probabilities;
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
        return 0.0;
        // TODO: Implement this method
    }




}
