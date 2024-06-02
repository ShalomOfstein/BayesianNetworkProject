import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Factor {
    private Map<List<String>,Double> table;
    private List<Variable> variables;

    /**
     * Constructor for the Factor class
     * this constructor creates a factor from a variable and a list of evidence variables
     *
     * @param v the variable
     * @param evidence the list of evidence variables
     */
    public Factor(Variable v, List<String> evidence) {
        //get the probability table from the variables CPT object
        CPT cpt = v.getProbabilityTable();
        this.table = cpt.getTable();
        variables = new ArrayList<Variable>(v.getParents());
        variables.add(v);
        //remove the evidence variables from the list of variables
        for (String e : evidence) {
            String[] parts = e.split("=");
            String varName = parts[0];
            String varValue = parts[1];
            for (List<String> key : table.keySet()){
                for (String k : key) {
                    if(k.startsWith(varName + "=")&& !k.equals(varName + "=" + varValue) ){
                        table.remove(key);
                    }
                }

            }
        }
    }


}
