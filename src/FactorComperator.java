package src;

import java.util.Comparator;

public class FactorComperator implements Comparator<Factor> {
    @Override
    public int compare(Factor o1, Factor o2) {
        if(o1.getTable().size() > o2.getTable().size()) {
            return 1;
        } else if(o1.getTable().size() < o2.getTable().size()) {
            return -1;
        }else{
            int ASCIISum1 = 0;
            int ASCIISum2 = 0;
            for(Variable v : o1.getVariables()){
                for(char c : v.getName().toCharArray()){
                    ASCIISum1 += (int)c;
                }
            }
            for(Variable v : o2.getVariables()){
                for(char c : v.getName().toCharArray()){
                    ASCIISum1 += (int)c;
                }
            }
            return Integer.compare(ASCIISum1, ASCIISum2);
        }
    }
}
