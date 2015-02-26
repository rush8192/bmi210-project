
import java.util.*;

public class Main {

	public static void main(String[] args) {
		if (args.length > 2) {
		    System.out.println(args);
			System.out.println("Loading ontology from file: " + args[0]);
			System.out.println("Loading malady-symptoms from file: " + args[1]);
			System.out.println("Loading symptom-synonms from file: " + args[2]);
			EMOntology maladyOntology = new EMOntology(args[0], args[1], args[2]);
			Malady m = maladyOntology.getRootClass();
			System.out.println(m.getClassName());
			System.out.println(maladyOntology);
			
			while (true) {
			    String query = System.console().readLine("input a list of symptoms: ");
			    System.out.println("");
			    Set<QueryTerm> queryTerms =
			        QueryTerm.fromInputString(query, maladyOntology.allSypmtomNames);
			    List<MaladyMatch> matches = maladyOntology.getTopMatches(queryTerms, 5);
                System.out.println("");
			    System.out.println("Top match is : " + matches.get(0).malady.getClassName()
			        + " with score: " + matches.get(0).matchScore);
			    System.out.println("");
			}
		}
	}

}
