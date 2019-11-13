package com.example.ChordCalculator.Model.Entities.Rule;

import com.example.ChordCalculator.Model.Entities.Instrumental;
import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class NeighborStringDifSoundRule extends Rule {

    public NeighborStringDifSoundRule(){

    }
    public NeighborStringDifSoundRule(Instrumental instrument) {
        this.instrument = instrument;
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
