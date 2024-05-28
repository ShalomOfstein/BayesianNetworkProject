import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Factor {
    private Map<List<String>,Double> table;
    private List<Variable> variables;

    public Factor(Variable v, List<String> evidence) {
        //get the probability table from the variables CPT object
        CPT cpt = v.getProbabilityTable();
        this.table = cpt.getTable();
        variables = new ArrayList<Variable>(v.getParents());
        variables.add(v);
        //remove the evidence variables from the list of variables
        for (String e : evidence) {
            if(variables.contains(e)) {

            }
        }



    }
}
