package com.example.ChordCalculator.Model.Rule;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import java.util.List;

//out-of-date!!! Need delete uses from code. Instead this there are CatchPerfection levels.
@Entity
public class FirstSoundIsRootRule extends Rule{


    public FirstSoundIsRootRule() {
    }

    public FirstSoundIsRootRule(Instrumental instrumental){
        this.instrumental = instrumental;
    }
    @Override
    public boolean isValid(Catch c) {
        /*List<StringCatch> stringCatches = c.getStringCatches();
        int lastUsedStringIndex = -1;
        for(StringCatch sc : stringCatches){
            if(sc.getBund()!=-1){
                lastUsedStringIndex++;
            }
        }
        return stringCatches.get(lastUsedStringIndex).getSound().equals(c.getChord().getBaseSound());*/
       return true;
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        return true;
    }
}
