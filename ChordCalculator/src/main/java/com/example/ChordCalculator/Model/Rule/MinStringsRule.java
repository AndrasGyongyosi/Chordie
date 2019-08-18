package com.example.ChordCalculator.Model.Rule;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class MinStringsRule extends Rule {


    public MinStringsRule(Instrumental inst, int value) {
        instrumental = inst;
        this.value = value;
    }
    public MinStringsRule(){
        super();
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        int activeStrings = 0;
        boolean usedSection = true;
        for (StringCatch sc :
                stringCatchList) {
            if (sc.getBund()!=-1)
                if (usedSection) activeStrings++;
                else return false;
            else{
                usedSection=false;
            }
        }
        if (usedSection || activeStrings>=value) return true;
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
