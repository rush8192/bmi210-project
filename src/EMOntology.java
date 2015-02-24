
import java.io.*;
import java.util.*;

public class EMOntology extends Ontology {
    
    public static final String ROOT_CLASS = "root";
    
    private Malady rootClass;
    
    public HashMap<String, Malady> allMaladies; 
    
    public EMOntology (String ontologyFile, String maladyFile, String symptomFile) {
        super(ontologyFile, maladyFile, symptomFile);
        try {
            // read in classes, ontology structure, and symptom
            List<String> ontologyData = readFile(ontologyFile);
            List<String> maladySymptoms = readFile(maladyFile);
            List<String> symptomSynonyms = readFile(symptomFile);
            loadOntology(ontologyData);
            HashMap<String, Symptom> allSymptoms = loadSymptoms(symptomSynonyms);

            // link the two with final file content
            loadMaladySymptoms(maladySymptoms, allSymptoms);
            
        } catch (Exception e ) {
            e.printStackTrace();
            throw new Error();
        }
    }
    
    private void loadMaladySymptoms(List<String> fileContents,
                                    HashMap<String, Symptom> allSymptoms) {
        for (String symptomLine : fileContents) {
            String[] csvComponents = symptomLine.split(",");
            String symptomName = csvComponents[0];
            Symptom symptom = allSymptoms.get(symptomName);
            symptom = new Symptom(symptom, csvComponents);
            
            String className = csvComponents[1];
            Malady malady = allMaladies.get(className);
            if (malady == null) {
                System.out.println("Unknown malady: " + className);
                continue;
            }
            malady.addSymptom(symptom);
            System.out.println("Malady: " + malady.getClassName() 
                + " now has n symptoms: " + malady.getSymptons().size());
        }
    }
    
    private HashMap<String, Symptom> loadSymptoms(List<String> fileContents) {
        HashMap<String, Symptom> allSymptoms = new HashMap<String, Symptom>();
        // read in all symptoms and their synonyms
        for (String symptom : fileContents) {
            Symptom s = new Symptom(symptom);
            for (String synonym : s.getAllNames()) {
                if (allSymptoms.containsKey(synonym))
                    s = s.mergeWith(allSymptoms.get(synonym));
            }
            for (String synonym : s.getAllNames()) {
                allSymptoms.put(synonym, s);
            }
        }
        return allSymptoms;
    }
    
    private void loadOntology(List<String> fileContents) {
        this.allMaladies = new HashMap<String, Malady>();
        // set root node
        rootClass = new Malady(ROOT_CLASS);
        allMaladies.put(ROOT_CLASS, rootClass);
        rootClass.addParent(rootClass);
        // read all maladies in
        for (String ontLine : fileContents) {
            Malady m = new Malady(ontLine);
            allMaladies.put(m.getClassName(), m);
        }
        // set parent/child relationships
        for (String ontLine : fileContents) {
            String[] csvComponents = ontLine.split(",");
            Malady classM = allMaladies.get(csvComponents[0]);
            Malady superM = allMaladies.get(csvComponents[1]);
            if (superM == null) {
                System.out.println("unknown super class: " + csvComponents[1]);
                continue;
            }
            classM.addParent(superM);
            superM.addChild(classM);
        }
    }
    
    private List<String> readFile(String file) throws Exception {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader rd = new BufferedReader(new FileReader(file));
        String line = rd.readLine(); // first line is header
        while ((line = rd.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
    
    public Malady getRootClass() {
        return rootClass;
    }
    
    public String toString() {
        StringBuilder str = new StringBuilder();
        int level = 0;
        Set<Malady> current = new HashSet<Malady>();
        Set<Malady> next = new HashSet<Malady>();
        current.add(rootClass);
        while (true) {
            str.append("level: " + level + " : ");
            for (Malady m : current) {
                str.append(m.getClassName() + "=" + m.getPriorProb() + " ("
                    + m.getParents().get(0).getClassName() + ") , ");
                for (OntClass c : m.getChildren()) {
                    next.add((Malady) c);
                }
            }
            str.append("\n");
            if (next.size() == 0) {
                break;
            }
            current = next;
            next = new HashSet<Malady>();
            level++;
        }
        return str.toString();
    }
    
}