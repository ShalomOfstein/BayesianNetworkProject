import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Factor {
    private final Map<List<String>,Double> table;
    private final List<Variable> variables;

    /**
     * Constructor for the Factor class
     * this constructor creates a factor from a variable and a list of evidence variables
     *
     * @param v the variable
     * @param evidence the list of evidence variables
     */
    public Factor(Variable v, HashMap<String, String> evidence) {
        //get the probability table from the variables CPT object
        CPT cpt = v.getProbabilityTable();
        this.table = cpt.getTable();
        variables = new ArrayList<Variable>(v.getParents());
        variables.add(v);
        if(v.getName()== "D"){
            System.out.println("D");
        }

        // Remove the evidence variables from the list of variables
        List<List<String>> keysToRemove = new ArrayList<>();

        for (String e : evidence.keySet()) {
            String varValue = evidence.get(e);
            for (List<String> key : table.keySet()){
                for (String k : key) {
                    if(k.startsWith(e + "=")&& !k.equals(e + "=" + varValue) ){
                        keysToRemove.add(key);
                    }
                }

            }
        }
        //remove the keys from the table
        for (List<String> key : keysToRemove) {
            table.remove(key);
        }
        printFactor();
    }

    private void printFactor() {
        System.out.println("Factor: ");
        System.out.println("Variables: ");
        for (Variable v : variables) {
            System.out.print(v.getName() + " ");
        }
        System.out.println("\nTable: ");
        for (List<String> key : table.keySet()) {
            System.out.print(key + " : " + table.get(key) + "\n");
        }
    }

}
