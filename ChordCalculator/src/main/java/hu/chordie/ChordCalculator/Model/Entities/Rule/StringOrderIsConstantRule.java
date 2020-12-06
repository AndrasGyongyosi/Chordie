package hu.chordie.ChordCalculator.Model.Entities.Rule;

import hu.chordie.ChordCalculator.Model.StringCatch;
import hu.chordie.ChordCalculator.Model.Entities.Instrument;
import hu.chordie.ChordCalculator.Model.Entities.MString;

import javax.persistence.Entity;
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
