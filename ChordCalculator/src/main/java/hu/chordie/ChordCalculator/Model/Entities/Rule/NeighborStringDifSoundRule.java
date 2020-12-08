package hu.chordie.chordCalculator.model.entities.rule;

import javax.persistence.Entity;

import hu.chordie.chordCalculator.model.Sound;
import hu.chordie.chordCalculator.model.StringCatch;
import hu.chordie.chordCalculator.model.entities.Instrument;

import java.util.List;

@Entity
public class NeighborStringDifSoundRule extends Rule {

    public NeighborStringDifSoundRule(){

    }
    public NeighborStringDifSoundRule(Instrument instrument) {
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
