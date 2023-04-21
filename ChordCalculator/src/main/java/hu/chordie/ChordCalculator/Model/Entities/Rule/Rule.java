package hu.chordie.ChordCalculator.Model.Entities.Rule;

import javax.persistence.*;

import hu.chordie.ChordCalculator.Model.Catch;
import hu.chordie.ChordCalculator.Model.StringCatch;
import hu.chordie.ChordCalculator.Model.Entities.Instrument;

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
