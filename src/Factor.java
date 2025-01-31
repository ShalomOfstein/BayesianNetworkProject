package src;

import java.util.*;

/**
 * Factor
 * This class represents a factor in a Bayesian Network.
 * A factor is a function that maps an assignment of values to a set of variables to a real number.
 * The factor is represented as a table that maps a set of values to a real number.
 * I.E: P(A=a, B=b,...) = x where x<=1 is a real number. and A, B, ... are variables in the network with outcomes a, b, ...
 */
public class Factor implements Comparable<Factor>{
    // the table of the factor is stored as a map of a set of strings to a real number
    // each set of strings represents an assignment of values to the variables in the factor
    private Map<Set<String>,Double> table;
    // the variables in the factor
    private Map<String,Variable> variables;

    /**
     * Constructor - creates a factor from a set of variables and a table
     * we use this constructor when we oin two factors or eliminate a variable
     * @param vars the variables in the factor
     * @param table the table of the factor
     */
    public Factor(Set<Variable> vars, Map<Set<String>,Double> table) {
        this.variables = new HashMap<String,Variable>();
        for(Variable v : vars){
            this.variables.put(v.getName(),v);
        }
        this.table = table;
    }
    /**
     * Constructor - creates a factor from a variable
     * we use this constructor when we initially create a factor from a single variable
     * the table is created from the CPT of the variable
     * @param v the variable in the factor
     */
    public Factor(Variable v){

        //create a set of variables this factor contains
        this.variables = new HashMap<String,Variable>();
        this.variables.put(v.getName(),v);
        for(Variable parent : v.getParents()){
            this.variables.put(parent.getName(),parent);
        }

        //create a table for the factor from the CPT of the variable
        this.table = new HashMap<Set<String>,Double>();
        CPT cpt = v.getProbabilityTable();
        // Change the key from a list to a set
        for(List<String> key : cpt.getTable().keySet()) {
            Set<String> newKey = new HashSet<String>(key);
            table.put(newKey, cpt.getTable().get(key));
        }


    }


    /**
     * Getters:
     */
    public Map<Set<String>,Double> getTable() {
        return table;
    }
    public Collection<Variable> getVariables() {
        return variables.values();
    }

    public double getProbability(String outcome) {
        for(Set<String> key : table.keySet()) {
            if(key.contains(outcome)) {
                return table.get(key);
            }
        }
        return 0.0;
    }

    /**
     * print the factor
     */
    public void printFactor() {
        System.out.println();
        System.out.println("Factor:");
        for (Variable v : variables.values()) {
            System.out.print(v.getName()+", ");
        }
        System.out.println("\nsize: "+table.size());
        for (Set<String> key : table.keySet()) {
            System.out.println(key + " : " + table.get(key));
        }
    }


    /**
     * Eliminate the Evidence Variables from this factor
     * @param evidence a map of evidence variables and their values
     */
    public void eliminateEvidence(Map<String,String> evidence) {
        if(evidence.isEmpty()) {
            return;
        }
        Set<String> evidenceStrings = new HashSet<>();
        for(String key : evidence.keySet()) {
            if(variables.containsKey(key)) {
                evidenceStrings.add(key + "=" + evidence.get(key));
            }
        }
        // Create a new table and a new list of variables
        Map<String,Variable> newVariables = new HashMap<String,Variable>();

        // Iterate over the table and remove anything but the evidence variables
        for(String e : evidenceStrings) {
            Map<Set<String>,Double> newTable= new HashMap<Set<String>,Double>();
            for (Set<String> key : table.keySet()) {
                if (key.contains(e)) {
                    Set<String> newKey = new HashSet<String>(key);
                    newKey.remove(e);
                    newTable.put(newKey, table.get(key));
                }
            }
            table = newTable;
        }

        // Update the variables
        for(String key : variables.keySet()) {
            if(!evidence.containsKey(key)) {
                newVariables.put(key,variables.get(key));
            }
        }
        this.variables = newVariables;
    }

    /**
     * join two factors on a hidden variable
     * @param f2 the factor to join with
     * @param hidden the hidden variable to join on
     * @return the new factor
     */
    public Factor join(Factor f2, Variable hidden){
        List<Variable> varsToJoin = new ArrayList<Variable>();
        varsToJoin.add(hidden);
        for(Variable v : f2.getVariables()){
            if(this.variables.containsKey(v.getName())){
                varsToJoin.add(v);
            }
        }


        // Create a new factor
        Set<Variable> newVars = new HashSet<>();
        newVars.addAll(this.getVariables());
        newVars.addAll(f2.getVariables());

        // Create a new table
        Map<Set<String>,Double> newTable = new HashMap<Set<String>,Double>();

        for(Set<String> key1 : this.table.keySet()) {
            Set<String> joinOutcomes = new HashSet<>();
            for(Variable v : varsToJoin){
                for(String outcome : v.getOutcomes()){
                    String outString = v.getName() + "=" + outcome;
                    if(key1.contains(outString)){
                        joinOutcomes.add(outString);
                    }
                }
            }
            for(Set<String> key2 : f2.getTable().keySet()) {
                if(contains(key2, joinOutcomes)){
                    Set<String> newKey = new HashSet<>(key1);
                    newKey.addAll(key2);
                    newTable.put(newKey, this.table.get(key1) * f2.getTable().get(key2));
                }
            }
        }
        return new Factor(newVars, newTable);
    }

    /**
     * Check if a set of strings (Key1) contains another set of strings (Key2)
     * @param key1 the set to check
     * @param key2 the set to check for
     * @return true if key1 contains key2, false otherwise
     */
    private boolean contains(Set<String> key1, Set<String> key2){
        for(String s : key2){
            if(!key1.contains(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * Eliminate a variable from the factor
     * take the factor and sum out the variables
     * Shrinks a factor to a smaller one
     */

    public Factor EliminateVariable(Variable hidden) {
        if(!this.variables.containsKey(hidden.getName())) {
            throw new RuntimeException("Variable not in factor");
        }

        // Create a new factor
        Set<Variable> newVars = new HashSet<>(this.getVariables());
        newVars.remove(hidden);

        Map<Set<String>,Double> newTable = new HashMap<Set<String>,Double>();

        // Iterate over the table and sum out the hidden variable
        for(String outcome : hidden.getOutcomes()) {
            String hiddenOutcome = hidden.getName()+"="+outcome;
            for(Set<String> key : table.keySet()) {
                if(key.contains(hiddenOutcome)) {
                    Set<String> newKey = new HashSet<>(key);
                    newKey.remove(hiddenOutcome);
                    if(newTable.containsKey(newKey)) {
                        newTable.put(newKey, newTable.get(newKey) + table.get(key));
                    } else {
                        newTable.put(newKey, table.get(key));
                    }
                }
            }
        }
        return new Factor(newVars, newTable);
    }

    /**
     * Normalize the factor
     */
    public void normalize() {
        double sum = 0;
        for(Double d : table.values()) {
            sum += d;
        }
        Map<Set<String>,Double> newTable = new HashMap<Set<String>,Double>();
        for(Set<String> key : table.keySet()) {
            newTable.put(key, table.get(key)/sum);
        }
        // Update the table
        this.table = newTable;
    }

    /**
     * Compare two factors
     * @param f the object to be compared.
     */
    @Override
    public int compareTo(Factor f) {
        if(this.table.size() > f.getTable().size()) {
            return 1;
        } else if(this.table.size() < f.getTable().size()) {
            return -1;
        }else{
            int ASCIISum1 = 0;
            int ASCIISum2 = 0;
            for(Variable v : this.variables.values()){
                for(char c : v.getName().toCharArray()){
                    ASCIISum1 += (int)c;
                }
            }
            for(Variable v : f.getVariables()){
                for(char c : v.getName().toCharArray()){
                    ASCIISum1 += (int)c;
                }
            }
            return Integer.compare(ASCIISum1, ASCIISum2);
        }
    }
    /**
     * To String
     */
     public String toString() {
         String s = "Variables: ";
         for (Variable v : variables.values()) {
             s += v.getName() + ", ";
         }
         s += "\n Table: \n";
         for (Set<String> key : table.keySet()) {
             s += key + " : " + table.get(key) + "\n";
         }
         return s;
     }
}
