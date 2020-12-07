package hu.chordie.chordCalculator.model.entities.rule;

import javax.persistence.Entity;

import hu.chordie.chordCalculator.model.StringCatch;
import hu.chordie.chordCalculator.model.entities.Instrument;
import hu.chordie.chordCalculator.model.entities.MString;

import java.util.List;

@Entity
public class StringOrderIsConstantRule extends Rule {
    public StringOrderIsConstantRule(Instrument inst, int value) {
        instrument = inst;
        this.value = value;
    }

    public StringOrderIsConstantRule() {
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        if (value!=0){
            List<MString> mStrings = instrument.getMStrings();
            for(int i=0; i< stringCatchList.size()-1;i++){
                double actualStringFrequency = mStrings.get(i).getFrequency();
                double nextStringFrequency = mStrings.get(i+1).getFrequency();
                double actualCatchFrequency = stringCatchList.get(i).getFrequency();
                double nextCatchFrequency = stringCatchList.get(i+1).getFrequency();

                if ((actualStringFrequency-nextStringFrequency>=0 && actualCatchFrequency-nextCatchFrequency<0)
                    || (actualStringFrequency-nextStringFrequency<0 && actualCatchFrequency-nextCatchFrequency>=0)){
                    return false;
                }
            }
        }
        return true;
    }
}
