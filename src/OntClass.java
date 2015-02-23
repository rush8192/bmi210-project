
import java.util.*;

public class OntClass {

	private String className;
	private List<OntClass> parents;
	private List<OntClas> children;

	public OntClass(String fileContents) {

	}

	public String getClassName() {
		return className;
	}

	public List<OntClass> getParents() {
		return parents;
	}

	public List<OntClass> getChildren() {
		return children;
	}
}
