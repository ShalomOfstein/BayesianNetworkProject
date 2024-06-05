package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Variable class
 * This class represents a single variable in a Bayesian Network.
 */

public class Variable {
    private final String name;
    private final ArrayList<Variable> parents;
    private final ArrayList<Variable> children;
    private List<String> outcomes;
    private CPT CPT;
    boolean isEvidence = false;
    int observed = 0;

    /**
     * Constructor
     */

    public Variable(String name) {
        this.name = name;
        parents = new ArrayList<Variable>();
        children = new ArrayList<Variable>();
        outcomes = new ArrayList<String>();
        CPT = null;
    }

    /**
     * Getters
     */
    public String getName() {
        return name;
    }
    public ArrayList<Variable> getParents() {
        return parents;
    }
    public ArrayList<Variable> getChildren() {
        return children;
    }
    public List<String> getOutcomes() {
        return outcomes;
    }
    public CPT getProbabilityTable() {
        return CPT;
    }

    /**
     * Setters
     */
    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }
    public void setProbabilities(CPT probabilities) {
        this.CPT = probabilities;
    }
    public void addParent(Variable v) {
        parents.add(v);
        v.addChild(this);
    }
    public void addChild(Variable v) {
        children.add(v);
    }

    /**
     * These are the methods used in the Bayes Ball algorithm
     * each Bayes Ball query sets different variables to be evidence
     * and during the Bayes Ball traversal, the variables are marked as observed
     */
    public void setEvidence(boolean isEvidence) {
        this.isEvidence = isEvidence;
    }
    public boolean isEvidence() {
        return isEvidence;
    }
    public void setObserved(int observed) {
        this.observed = observed;
    }
    public int Observed() {
        return observed;
    }


    // Remove a parent from the variable
    public void removeParent(Variable v) {
        parents.remove(v);
        v.children.remove(this);
    }
    // get the probability of the variable given the outcomes of its parents
    public double getProbability(ArrayList<String> parentOutcomes) {
        return CPT.getProbability(parentOutcomes);
    }




}
