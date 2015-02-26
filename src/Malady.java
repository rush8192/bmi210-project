
import java.util.*;

public class Malady extends OntClass {

    private Set<Symptom> symptoms;
    private Set<Symptom> inheritedSymptoms;
    private double priorProbability;

    public Malady() {}

    public Malady(String line) {
        super(line);
        symptoms = new HashSet<Symptom>();
        String[] csvComponents = line.split(",");
        this.className = csvComponents[0];
        inheritedSymptoms = null;
        if (csvComponents.length == 3)
            priorProbability = Double.parseDouble(csvComponents[2]);
        else
            priorProbability = 0.0;
    }
    
    public static Malady fromSymptomLine(String[] symptomLine) {
        Malady m = new Malady(symptomLine[1]);
        m.priorProbability = 1.0; // placeholder for now
        return m;
    }

    void addSymptom(Symptom s) {
        symptoms.add(s);
    }
    
    double getPriorProb() {
        return priorProbability;
    }

    public Set<Symptom> getSymptons() {
        return symptoms;
    }
    
    public void addSympton(Symptom s) {
        symptoms.add(s);
    }
    
    public double getMatchScore(Set<QueryTerm> query) {
        Set<Symptom> allSymptoms = new HashSet<Symptom>();
        allSymptoms.addAll(inheritedSymptoms());
        allSymptoms.addAll(symptoms);
        int totalMaxScore = Symptom.MAX_SCORE*allSymptoms.size();
        int evokeS = 0;
        int unevokedScore = 0;
        for (Symptom symp : allSymptoms) {
            if (symp.matchesQuery(query)) {
                evokeS += symp.evokedScore();
            } else {
                unevokedScore += symp.frequencyPenalty();
            }
        }
        return priorProbability*(evokeS - unevokedScore);
    }
    
    Set<Symptom> inheritedSymptoms() {
        if (inheritedSymptoms == null) {
            inheritedSymptoms = new HashSet<Symptom>();
            Set<OntClass> current = new HashSet<OntClass>();
            current.addAll(this.getParents());
            while (true) {
                Set<OntClass> next = new HashSet<OntClass>();
                for (OntClass classObj : current) {
                    Malady malady = (Malady)classObj;
                    inheritedSymptoms.addAll(malady.getSymptons());
                    next.addAll(malady.getParents());
                }
                // root's only parent is itself, so if next==current we have
                // reached the root node
                if (next.equals(current)) {
                    break;
                }
                current = next;
            }
        }
        return inheritedSymptoms;
    }

}