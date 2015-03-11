
import java.util.*;

public class Symptom {
    
    public static final int MAX_SCORE = 10;
    
    private List<String> synonyms;
    public int importN;
    public int frequencyN;
    public int evokeN;
    
    public Symptom(String line) {
        String[] csvComponents = line.split(",");
        this.synonyms = new ArrayList<String>();
        for (String syn : csvComponents) {
            synonyms.add(syn);
        }
        importN = -1;
        frequencyN = -1;
        evokeN = -1;
    }
    
    public Symptom(Symptom other, String[] line) {
        synonyms = new ArrayList<String>();
        if (other !=  null) {
            for (String syn : other.synonyms) {
                if (!synonyms.contains(syn))
                    synonyms.add(syn);
            }
        }
        if (!synonyms.contains(line[0]))
            synonyms.add(line[0]);
        importN = Integer.parseInt(line[2]);
        frequencyN = Integer.parseInt(line[3]);
        evokeN = Integer.parseInt(line[4]);
    }
    
    public int evokedScore() {
        return 3*evokeN;
        /* switch (evokeN) {
            case 10:
            
                break;
            case 9:
                
                break;
            case 8:
            
            case 7:
                return 0;
        }
        return 0;*/
    }
    
    public int frequencyPenalty() {
        return frequencyN;
    }
    
    public int importPenalty() {
        return importN;
    }
    
    public Symptom mergeWith(Symptom other) {
        for (String syn : synonyms) {
            if (!synonyms.contains(syn))
                other.synonyms.add(syn);
        }
        return other;
    }
    
    List<String> getAllNames() {
        return synonyms;
    }
    
    public String getName() {
        if (synonyms.size() == 0) return "UNK";
        return synonyms.get(0);
    }
    
    public String toString() {
        String finalS = "";
        for (int i = 0; i < synonyms.size(); i++) {
            finalS += synonyms.get(i);
            if (i != synonyms.size() - 1)
                finalS += ", ";
            if (i == synonyms.size() - 2)
                finalS += "or ";
        }
        return finalS;
    }
    
    @Override
    public boolean equals(Object other) {
        return ((Symptom)other).synonyms.equals(this.synonyms);
    }
    
    @Override
    public int hashCode() {
        return synonyms.hashCode();
    }
    
    public QueryTerm matchesQuery(Set<QueryTerm> query) {
        for (QueryTerm term : query) {
            for (String synonym : synonyms) {
                if (term.matches(synonym)) {
                    return term;
                }
            }
        }
        return null;
    }
}