package com.example.ChordCalculator.Model.Rule;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.MString;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class StringOrderIsConstantRule extends Rule {
    public StringOrderIsConstantRule(Instrumental inst, int value) {
        instrumental = inst;
        this.value = value;
    }

    public StringOrderIsConstantRule() {
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        if (value!=0){
            List<MString> mStrings = instrumental.getMStrings();
            for(int i=0; i< stringCatchList.size()-1;i++){
                //System.out.println("prev: "+mStrings.get(i).getName());
                double actualStringFrequency = mStrings.get(i).getFrequency();
                double nextStringFrequency = mStrings.get(i+1).getFrequency();
                double actualCatchFrequency = stringCatchList.get(i).getFrequency();
                //System.out.println("next: "+mStrings.get(i+1).getName());
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
