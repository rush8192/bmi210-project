

public class Main {

	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.println("Loading ontology from file: " + args[0]);
			Ontology maladyOntology = new Ontology(args[0]);
		}
	}

}
