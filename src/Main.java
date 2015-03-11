
import java.util.*;

public class Main {

    public static final int NUM_MATCHES = 5;
    
    // threshold constants
    public static final int FILTER_THRESHOLD = 15;
    public static final int ABS_DIFF_THRESHOLD = 8;
    public static final int ABOVE_ZERO_THRESHOLD = 4;
    
    public static final int FOLLOWUP_QUESTIONS = 2;

	public static void main(String[] args) {
		if (args.length > 2) {
		    System.out.println(args);
			System.out.println("Loading ontology from file: " + args[0]);
			System.out.println("Loading malady-symptoms from file: " + args[1]);
			System.out.println("Loading symptom-synonms from file: " + args[2]);
			
			// load and print out ontology
			EMOntology maladyOntology = new EMOntology(args[0], args[1], args[2]);
			System.out.println(maladyOntology);
			
			System.out.println("\n\n############ Welcome to HikeAid #############\n\n");
			// process user queries until they exit the program for now
			while (true) {
			    String query = System.console().readLine("#### Input a list of symptoms (space separated): ####\n -> ");
			    Set<QueryTerm> queryTerms =
			        QueryTerm.fromInputString(query, maladyOntology.allImportScores);
			    if (queryTerms.size() == 0) {
			        System.out.println("Sorry, we didn't recognize any of your symptoms. Please try again.");
			        continue;
			    }
			    System.out.println("");
			    List<MaladyMatch> matches = maladyOntology.getTopMatches(queryTerms, 5);
			    System.out.println("Top match is: " + matches.get(0).malady.getClassName());
                while (!aboveThreshold(matches.get(0), matches.get(1))) {
                    System.out.println("Second match is: " + matches.get(1).malady.getClassName());
                    System.out.println("Refining matches...");
                    List<MaladyMatch> refined = refineMatches(matches);
                    if (refined == null) {
                        System.out.println("CAUTION: unable to disambiguate between maladies with high confidence");
                        break;
                    }
                    matches = refined;
                    System.out.println("Top match is: " + matches.get(0).malady.getClassName());
                }
                System.out.println("Suggested treatments: " + matches.get(0).malady.getTreatments());
			    System.out.println("");
			}
		}
	}
	
	private static List<MaladyMatch> refineMatches(List<MaladyMatch> initialMatches) {
	    initialMatches = filterMatches(initialMatches);
	
	    Set<Symptom> unmatchedUnique = new HashSet<Symptom>(initialMatches.get(0).unmatchedSymptoms);
	    unmatchedUnique.removeAll(initialMatches.get(1).unmatchedSymptoms);

        Set<Symptom> unmatchedUnique2 = null;
        for (int i = 0; i < 10; i++) {
            int randomMatchIndex = 1 + ((new Random()).nextInt(initialMatches.size() - 1));
            unmatchedUnique2 = new HashSet<Symptom>(initialMatches.get(randomMatchIndex).unmatchedSymptoms);
            unmatchedUnique2.removeAll(initialMatches.get(0).unmatchedSymptoms);
            if (unmatchedUnique2.size() > 0) break;
        }
        
        if (unmatchedUnique.size() == 0 && unmatchedUnique2.size() == 0) {
            //System.out.println("Unable to disambuguate between top matches");
            return null;
        }
	    
	    int i = 0;
	    for (Symptom symp : unmatchedUnique) {
	        if ("urgent".equals(symp.getName()) || "emergency".equals(symp.getName()))
	            continue;
	        askFollowup(symp, initialMatches);
	        i++;
	        break;
	    }
	    
	    for (Symptom symp : unmatchedUnique2) {
	        if ("urgent".equals(symp.getName()) || "emergency".equals(symp.getName()))
	            continue;
	        if (i >= FOLLOWUP_QUESTIONS)
	            break;
	        askFollowup(symp, initialMatches);
	        i++;
	    }
	    if (i == 0)
	        return null;
	    PriorityQueue<MaladyMatch> sortedMaladies = new PriorityQueue<MaladyMatch>(initialMatches);
	    List<MaladyMatch> toReturn = new ArrayList<MaladyMatch>(sortedMaladies);
	    aboveThreshold(toReturn.get(0), toReturn.get(1));
	    return toReturn;
	}
	
	private static void askFollowup(Symptom symp, List<MaladyMatch> initialMatches) {
	    System.out.println(" Do any of the following describe your condition (y/n): " +
	            symp.toString());
        String response = System.console().readLine(" -> ").toLowerCase();
        if (response.equals("y") || response.contains("yes")) {
            for (MaladyMatch match : initialMatches) {
                match.addMatchingSymptom(symp);
            }
        } else {
            for (MaladyMatch match : initialMatches) {
                match.markExplicitNonMatch(symp);
            }
        }
	}
	
	private static List<MaladyMatch> filterMatches(List<MaladyMatch> matches) {
	    List<MaladyMatch> filtered = new ArrayList<MaladyMatch>();
	    filtered.add(matches.get(0));
	    double firstScore = matches.get(0).getMatchScore();
	    for (int i = 1; i < matches.size(); i++) {
	        if ((firstScore - matches.get(i).getMatchScore()) <= FILTER_THRESHOLD)
	            filtered.add(matches.get(i));
	    }
	    return filtered;
	}
	
	private static boolean aboveThreshold(MaladyMatch first, MaladyMatch second) {
	    double firstScore = first.getMatchScore();
	    double secondScore = second.getMatchScore();
	    double absDiff = firstScore - secondScore;
	    if (absDiff > ABS_DIFF_THRESHOLD)
	        return true;
	    if (firstScore > ABOVE_ZERO_THRESHOLD && secondScore < 0)
	        return true;
	    System.out.println("Top score: " + firstScore + " second: " + secondScore); 
	    return false;
	}

}
