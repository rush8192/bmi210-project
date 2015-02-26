
import java.util.*;

public abstract class OntClass {

	protected String className;
	private List<OntClass> parents;
	private List<OntClass> children;

    public OntClass() {}

	public OntClass(String className) {
        this.className = className;
        parents = new ArrayList<OntClass>();
        children = new ArrayList<OntClass>();
	}

    void addParent(OntClass parent) {
        parents.add(parent);
    }
    
    void addChild(OntClass child) {
        children.add(child);
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
	
	public abstract double getMatchScore(Set<QueryTerm> query);
}
