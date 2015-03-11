
import java.util.*;

public class MaladyMatch implements Comparable<MaladyMatch> {

    public Malady malady;
    public Set<QueryTerm> terms;
    
    public double matchScore;
    private boolean initialized;
    
    Set<Symptom> matchedSymptoms = null;
    Set<Symptom> unmatchedSymptoms = null;
    Set<Symptom> explicitUnmatched = null;
    Set<QueryTerm> unmatchedTerms = null;
    
    public MaladyMatch(Malady m, Set<QueryTerm> terms) {
        this.malady = m;
        this.terms = terms;
        initialized = false;
    }
    
    void addMatchingSymptom(Symptom s) {
        if (unmatchedSymptoms.contains(s)) {
            unmatchedSymptoms.remove(s);
            matchedSymptoms.add(s);
        }
    }
    
    void markExplicitNonMatch(Symptom s) {
        if (unmatchedSymptoms.contains(s)) {
            unmatchedSymptoms.remove(s);
            explicitUnmatched.add(s);
        } 
    }
    
    double getMatchScore() {
        if (!initialized) {
            Set<Symptom> allSymptoms = new HashSet<Symptom>();
            unmatchedSymptoms = new HashSet<Symptom>();
            matchedSymptoms = new HashSet<Symptom>();
            explicitUnmatched = new HashSet<Symptom>();
            allSymptoms.addAll(malady.inheritedSymptoms());
            allSymptoms.addAll(malady.symptoms);
            Set<QueryTerm> unmatchedTerms = new HashSet<QueryTerm>(terms);
            for (Symptom symp : allSymptoms) {
                QueryTerm matchingTerm;
                if ((matchingTerm = symp.matchesQuery(terms)) != null) {
                    unmatchedTerms.remove(matchingTerm);
                    matchedSymptoms.add(symp);
                } else {
                    unmatchedSymptoms.add(symp);
                }
            }
            this.unmatchedTerms = unmatchedTerms;
            initialized = true;
        }
        int evokeS = 0;
        int unevokedScore = 0;
        int importS = 0;
        for (Symptom symp : matchedSymptoms) {
            evokeS += symp.evokedScore();
        }
        for (Symptom symp : unmatchedSymptoms) {
            unevokedScore += symp.frequencyPenalty();
        }
        for (Symptom symp : explicitUnmatched) {
            unevokedScore += symp.frequencyPenalty();
        }
        for (QueryTerm t : unmatchedTerms) {
            importS += t.importPenalty();
        }
        matchScore = malady.getPriorProb()*(evokeS - unevokedScore - importS);
        return matchScore;
    }
    
    @Override
    public int compareTo(MaladyMatch other) {
        Double ms = new Double(getMatchScore());
        return -1*(ms.compareTo(other.getMatchScore()));
    }


}