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
    private CPT CPT;
    boolean isEvidence = false;
    int observed = 0;

    public Variable(String name) {
        this.name = name;
        parents = new ArrayList<Variable>();
        children = new ArrayList<Variable>();
        outcomes = null;
        CPT = null;
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
    public CPT getProbabilityTable() {
        return CPT;
    }



    public void setOutcomes(String[] outcomes) {
        this.outcomes = outcomes;
    }
    public void setProbabilities(CPT probabilities) {
        this.CPT = probabilities;
    }

    public void addParent(Variable v) {
        parents.add(v);
        v.children.add(this);
    }

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

    public void removeParent(Variable v) {
        parents.remove(v);
        v.children.remove(this);
    }
    public double getProbability(String[] parentOutcomes) {
        return CPT.getProbability(parentOutcomes);
    }




}
