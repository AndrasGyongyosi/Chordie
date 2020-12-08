package hu.chordie.chordCalculator.model.entities.rule;

import javax.persistence.Entity;

import hu.chordie.chordCalculator.model.StringCatch;
import hu.chordie.chordCalculator.model.entities.Instrument;

import java.util.List;

@Entity
public class MaxBundDifRule extends Rule {

    public MaxBundDifRule(Instrument inst, int value) {
        instrument = inst;
        this.value = value;
    }

    public MaxBundDifRule() {
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        int max=1;
        int min= instrument.getBundNumber();

        for (StringCatch sc:
                stringCatchList) {
            int actualBund = sc.getBund();
            if (actualBund>max) max=actualBund;
            if (actualBund<min && actualBund>0) min = actualBund;
        }
        return (max-min+1<=value);
    }
}
