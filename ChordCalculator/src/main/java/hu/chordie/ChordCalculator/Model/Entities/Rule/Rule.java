package hu.chordie.chordCalculator.model.entities.rule;

import javax.persistence.*;

import hu.chordie.chordCalculator.model.Catch;
import hu.chordie.chordCalculator.model.StringCatch;
import hu.chordie.chordCalculator.model.entities.Instrument;

import java.util.List;

@Entity
public abstract class Rule {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    protected Instrument instrument;

    protected int value;
    public boolean isValid(Catch c){
        return isValid(c.getStringCatches());
    }
    public abstract boolean isValid(List<StringCatch> stringCatchList);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @PreRemove
    private void beforeRemove(){
        instrument = null;
    }
}
