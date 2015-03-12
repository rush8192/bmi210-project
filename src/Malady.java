
import java.util.*;

public class Malady extends OntClass {

    Set<Symptom> symptoms;
    private Set<Symptom> inheritedSymptoms;
    private double priorProbability;
    public String treatment;

    public Malady() {}

    public Malady(String line) {
        super(line);
        symptoms = new HashSet<Symptom>();
        String[] csvComponents = line.split(",");
        this.className = csvComponents[0];
        inheritedSymptoms = null;
        if (csvComponents.length >= 3)
            priorProbability = Double.parseDouble(csvComponents[2]);
        else
            priorProbability = 0.0;
            
        if (csvComponents.length >= 4) {
            String[] treatments = Arrays.copyOfRange(csvComponents, 3, csvComponents.length);
            treatment = "";
            for (String t : treatments) {
                treatment += t + ",";
            }
            treatment = treatment.substring(0, treatment.length() - 1);
        } else
            treatment = null;
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
    
    public String getTreatments() {
        String treatments = "";
        String emergency = null;
        Set<OntClass> current = new HashSet<OntClass>();
        current.add(this);
        while (true) {
            Set<OntClass> next = new HashSet<OntClass>();
            for (OntClass classObj : current) {
                Malady malady = (Malady)classObj;
                if (malady.treatment != null && !"".equals(malady.treatment)) {
                    if (malady.treatment.contains("Call 911 immediately.")) {
                        emergency = malady.treatment;
                    } else 
                        treatments += " " + malady.treatment + ",";
                }
                next.addAll(malady.getParents());
            }
            // root's only parent is itself, so if next==current we have
            // reached the root node
            if (next.equals(current)) {
                break;
            }
            current = next;
        }
        if (treatments.length() > 0 && treatments.charAt(treatments.length() - 1) == ',')
            treatments = treatments.substring(0, treatments.length() - 1);
        return emergency == null ? treatments : emergency + " In the meantime: " + treatments;
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