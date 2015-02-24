
import java.util.*;

public class Malady extends OntClass {

    private List<Symptom> symptoms;
    private double priorProbability;

    public Malady(String line) {
        super(line);
        symptoms = new ArrayList<Symptom>();
        String[] csvComponents = line.split(",");
        this.className = csvComponents[0];
        if (csvComponents.length == 3)
            priorProbability = Double.parseDouble(csvComponents[2]);
        else
            priorProbability = 0.0;
    }

    void addSymptom(Symptom s) {
        symptoms.add(s);
    }
    
    double getPriorProb() {
        return priorProbability;
    }

    public List<Symptom> getSymptons() {
        return symptoms;
    }
    
    public void addSympton(Symptom s) {
        symptoms.add(s);
    }
    
    public double getMatchScore(String queryString) {
        return 0.0;
    }

}