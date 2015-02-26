
public class MaladyMatch implements Comparable<MaladyMatch> {

    public Malady malady;
    public double matchScore;
    
    public MaladyMatch(Malady m, int score) {
        this.malady = m;
        this.matchScore = score;
    }
    
    @Override
    public int compareTo(MaladyMatch other) {
        Double ms = new Double(matchScore);
        return -1*(ms.compareTo(other.matchScore));
    }


}