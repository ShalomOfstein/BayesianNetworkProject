import java.util.*;

public class Factor {
    private Map<Set<String>,Double> table;
    private Map<String,Variable> variables;

    public Factor(List<Variable> vars, Map<Set<String>,Double> table) {
        this.variables = new HashMap<String,Variable>();
        for(Variable v : vars){
            this.variables.put(v.getName(),v);
        }
        this.table = table;
    }

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
        System.out.println("Factor:");
        for (Variable v : variables.values()) {
            System.out.println(v.getName());
        }
        for (Set<String> key : table.keySet()) {
            System.out.println(key + " : " + table.get(key));
        }
    }


    /**
     * Eliminate the Evidence Variables from this factor
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
            if(!evidenceStrings.contains(key)) {
                newVariables.put(key,variables.get(key));
            }
        }
        this.variables = newVariables;
    }





}
