package hu.chordie.ChordCalculator.Model.Entities.Rule;

import javax.persistence.Entity;

import hu.chordie.ChordCalculator.Model.StringCatch;
import hu.chordie.ChordCalculator.Model.Entities.Instrument;

import java.util.List;

@Entity
public class MinStringsRule extends Rule {


    public MinStringsRule(Instrument inst, int value) {
        instrument = inst;
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
