package hu.chordie.ChordCalculator.Model.Entities.Rule;

import hu.chordie.ChordCalculator.Model.Sound;
import hu.chordie.ChordCalculator.Model.StringCatch;
import hu.chordie.ChordCalculator.Model.Entities.Instrument;

import javax.persistence.Entity;
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
