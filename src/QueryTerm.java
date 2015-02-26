
import java.util.*;

public class QueryTerm {

    private String term;
    
    public QueryTerm(String s) {
        term = s;
    }
    
    public boolean matches(String other) {
        return term.equals(other);
    }
    
    public String getTerm() {
        return term;
    }
    
    public int hashCode() {
        return term.hashCode();
    }
    
    public boolean equals(QueryTerm other) {
        return term.equals(other.getTerm());
    }
    
    public static Set<QueryTerm> fromInputString(String input, Set<String> knownSymptoms) {
        Set<QueryTerm> queryTerms = new HashSet<QueryTerm>();
        String[] splitInput = input.split(" ");
        BitSet usedTokens = new BitSet(splitInput.length);
        for (int i = splitInput.length; i > 0; i--) {
            for (int startIndex = 0; startIndex + i <= splitInput.length; startIndex++) {
                int endIndex = startIndex + i;
                if (!usedTokens.get(startIndex, endIndex).isEmpty()) continue;
                String combinedTerm = combinedTerm(splitInput, startIndex, endIndex);
                if (knownSymptoms.contains(combinedTerm)) {
                    QueryTerm qt = new QueryTerm(combinedTerm);
                    queryTerms.add(qt);
                    for (int index = startIndex; index < endIndex; index++) {
                        usedTokens.set(index, true);
                    }
                }
            }
        }
        return queryTerms;
    }
    
    private static String combinedTerm(String[] terms, int startIndex, int endIndex) {
        String combinedString = "";
        for (int i = startIndex; i < endIndex; i++) {
            combinedString += terms[i];
            if (i != endIndex - 1) {
                combinedString += " ";
            }
        }
        return combinedString;
    }
}