package com.example.ChordCalculator.Model.Rule;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class NeighborStringDifSoundRule extends Rule {

    public NeighborStringDifSoundRule(){

    }
    public NeighborStringDifSoundRule(Instrumental instrumental) {
        this.instrumental = instrumental;
    }

    @Override
    public boolean isValid(List<StringCatch> stringCatchList) {
        Sound lastSound=null;
        for(StringCatch stringCatch : stringCatchList){
            Sound sound = stringCatch.getSound();
            if(sound!=null && sound.equals(lastSound)) return false;
            lastSound = sound;
        }
        return true;
    }
}
