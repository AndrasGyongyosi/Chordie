package com.example.ChordCalculator.Model.Rule;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.*;
import java.util.List;

@Entity
public abstract class Rule {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    protected Instrumental instrument;

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
