
import java.util.*;

public class Symptom {
    
    private List<String> synonyms;
    private int importN;
    private int frequencyN;
    private int strengthN;
    
    public Symptom(String line) {
        String[] csvComponents = line.split(",");
        this.synonyms = new ArrayList<String>();
        for (String syn : csvComponents) {
            synonyms.add(syn);
        }
        importN = -1;
        frequencyN = -1;
        strengthN = -1;
    }
    
    public Symptom(Symptom other, String[] line) {
        synonyms = new ArrayList<String>();
        if (other !=  null) {
            for (String syn : other.synonyms) {
                synonyms.add(syn);
            }
        }
        importN = Integer.parseInt(line[3]);
        frequencyN = Integer.parseInt(line[4]);
        strengthN = Integer.parseInt(line[5]);
    }
    
    public Symptom mergeWith(Symptom other) {
        for (String syn : synonyms) {
            other.synonyms.add(syn);
        }
        return other;
    }
    
    List<String> getAllNames() {
        return synonyms;
    }
    
    public String getName() {
        return synonyms.get(0);
    }
    
    public boolean matchesQuery(List<String> query) {
        for (String name : query) {
            if (name.equals(query)) {
                return true;
            }
        }
        return false;
    }
}