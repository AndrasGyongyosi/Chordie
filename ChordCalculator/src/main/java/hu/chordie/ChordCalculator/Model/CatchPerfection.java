package hu.chordie.ChordCalculator.Model;

public enum CatchPerfection {
    LOW(1),    // Not all notes.
    MEDIUM(2), // All notes, root note is bad.
    HIGH(3);   // All notes, root note is fine.

    private Integer weight;
    private CatchPerfection(int weight){
        this.weight = weight;
    }

    public Integer getWeight() {
        return weight;
    }
}
