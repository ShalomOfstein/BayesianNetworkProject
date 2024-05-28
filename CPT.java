import java.util.*;

/**
 *  conditional probability table
 *
 */

public class CPT {
    private Map<List<String>,Double> table;
    private Variable v;

    public CPT(Variable v, ArrayList<Variable> parents, double[] probabilities) {
        this.v = v;
        table = new HashMap<List<String>,Double>();
        setCPTable(parents, probabilities);
        printCPT();
    }

    /**
     * Set the conditional probability table for the variable
     * given the outcomes of its parents
     *
     * @param parents list of parent variables
     * @param probabilities probabilities of the variable given the outcomes of its parents
     */

    public void setCPTable(ArrayList<Variable> parents, double[] probabilities) {
        List<List<String>> outcomesList = new ArrayList<>();

        // creating the 2D list of outcomes per parent
        if(parents.size() != 0) {
            for (Variable parent : parents) {
                List<String> outcomes = new ArrayList<>();
                for (String outcome : parent.getOutcomes()) {
                    outcomes.add(parent.getName() + "=" + outcome);
                }
                outcomesList.add(outcomes);
            }
        }
        // Add the outcomes of the variable itself
        List<String> varOutcomes = new ArrayList<>();
        for (String outcome : v.getOutcomes()) {
            varOutcomes.add(v.getName() + "=" + outcome);
        }
        outcomesList.add(varOutcomes);

        // Generate all combinations of outcomes
        List<List<String>> allCombinations = generateAllCombinations(outcomesList);



        // Map combinations to probabilities
        for (int i = 0; i < allCombinations.size(); i++) {
            table.put(allCombinations.get(i), probabilities[i]);
        }
    }

    /**
     * Generate all combinations of outcomes
     * @param outcomesList list of outcomes for each parent and the variable itself
     * @return list of all combinations of outcomes in the order to be mapped to probabilities
     */
    private List<List<String>> generateAllCombinations(List<List<String>> outcomesList) {
        List<List<String>> allCombinations = new ArrayList<>();
        generateAllCombinationsHelper(outcomesList, allCombinations, 0, new ArrayList<String>());
        return allCombinations;
    }
    /**
     * Helper function to generate all combinations of outcomes
     * @param outcomesList list of outcomes for each parent and the variable itself
     * @param allCombinations list of all combinations of outcomes in the order to be mapped to probabilities
     * @param index index of the parent in the list of outcomes
     * @param current current set of outcomes
     */
    private void generateAllCombinationsHelper(List<List<String>> outcomesList, List<List<String>> allCombinations, int index, List<String> current) {
        if (index == outcomesList.size()) {
            allCombinations.add(new ArrayList<>(current));
            return;
        }
        for (String outcome : outcomesList.get(index)) {
            current.add(outcome);
            generateAllCombinationsHelper(outcomesList, allCombinations, index + 1, current);
            current.remove(outcome);
        }
    }

    /**
     * Get the probability of the variable given the outcomes of its parents
     * @param parentOutcomes outcomes of the parents
     * @return probability of the variable given the outcomes of its parents
     */

    public double getProbability(String[] parentOutcomes) {
        Set<String> outcomeSet = new HashSet<>(Arrays.asList(parentOutcomes));
        return table.getOrDefault(outcomeSet, 0.0);
    }
    public Map<List<String>, Double> getTable() {
        return table;
    }

    public void printCPT() {
        System.out.println("CPT for " + v.getName());
        for (Map.Entry<List<String>, Double> entry : table.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }


}
