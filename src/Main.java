

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
		}
	}

}
