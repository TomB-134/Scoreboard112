package types;

public class ScoreboardEntry {
    public String criteriaName;
    public String displayName;
    public String name;

    public ScoreboardEntry(String criteriaName, String displayName, String name) {
        this.criteriaName = criteriaName;
        this.displayName = displayName;
        this.name = name;
    }
}
