
import java.util.*;

public class QueryTerm {

    private String term;
    private int importS;
    
    public QueryTerm(String s, int importScore) {
        term = s;
        importS = importScore;
    }
    
    public boolean matches(String other) {
        return term.equals(other);
    }
    
    public String getTerm() {
        return term;
    }
    
    public int importPenalty() {
        return importS;
    }
    
    @Override
    public int hashCode() {
        return term.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        return term.equals(((QueryTerm)other).getTerm());
    }
    
    public static Set<QueryTerm> fromInputString(String input, HashMap<String, Integer> allImportScores) {
        Set<QueryTerm> queryTerms = new HashSet<QueryTerm>();
        String[] splitInput = input.split(" ");
        BitSet usedTokens = new BitSet(splitInput.length);
        for (int i = splitInput.length; i > 0; i--) {
            for (int startIndex = 0; startIndex + i <= splitInput.length; startIndex++) {
                int endIndex = startIndex + i;
                if (!usedTokens.get(startIndex, endIndex).isEmpty()) continue;
                String combinedTerm = combinedTerm(splitInput, startIndex, endIndex);
                if (allImportScores.containsKey(combinedTerm)) {
                    QueryTerm qt = new QueryTerm(combinedTerm, allImportScores.get(combinedTerm));
                    queryTerms.add(qt);
                    for (int index = startIndex; index < endIndex; index++) {
                        usedTokens.set(index, true);
                    }
                }
            }
        }
        String match = "Matched terms: ";
        String unmatched = "Unmatched terms: ";
        for (int i = 0; i < splitInput.length; i++) {
            if (usedTokens.get(i))
                match += splitInput[i] + " ";
            else
                unmatched += splitInput[i] + " ";
        }
        System.out.println(match);
        System.out.println(unmatched);
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