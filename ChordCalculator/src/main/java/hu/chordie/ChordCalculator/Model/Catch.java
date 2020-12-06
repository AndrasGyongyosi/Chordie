package hu.chordie.ChordCalculator.Model;

import com.google.common.collect.Maps;

import hu.chordie.ChordCalculator.Model.Chord.Chord;
import hu.chordie.ChordCalculator.Model.Entities.Instrument;
import hu.chordie.ChordCalculator.Model.Entities.StoredCatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catch {
    private Chord chord;
    private Instrument instrument;
    private List<StringCatch> stringCatches;   //.size()=instumentals strings
    private int difficulty; //1-5
    private CatchPerfection perfection;

    public Catch(){

    }
    public Catch(Chord chord, Instrument instrument, List<StringCatch> stringCatches, int difficulty) {
        this.chord = chord;
        this.instrument = instrument;
        this.stringCatches = stringCatches;
        this.difficulty = difficulty;
    }
    public int getBundDif(){
        int maxBund = 0;
        int minBund = instrument.getBundNumber();
        for(StringCatch sc : stringCatches){
            if(sc.getBund()>maxBund){
                maxBund = sc.getBund();
            }
            if(sc.getBund()<minBund && sc.getBund()>0){
                minBund = sc.getBund();
            }
        }

        return maxBund-minBund+1;
    }
    public Chord getChord() {
        return chord;
    }

    public void setChord(Chord chord) {
        this.chord = chord;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public List<StringCatch> getStringCatches() {
        return stringCatches;
    }

    public void setStringCatches(ArrayList<StringCatch> stringCatches) {
        this.stringCatches = stringCatches;
    }
    public Map<Integer, List<StringCatch>> getStringCatchesByBund(){
        Map<Integer, List<StringCatch>> result = Maps.newHashMap();
        for(int bund=1; bund<=instrument.getBundNumber();bund++){
            List<StringCatch> actualScs = new ArrayList<>();
            for(StringCatch sc: stringCatches){
                if (sc.getBund()==bund){
                    actualScs.add(sc);
                }
            }
            result.put(bund, actualScs);
        }
        return result;
    }
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public CatchPerfection getPerfection() {
        return perfection;
    }

    public void setPerfection(CatchPerfection perfection) {
        this.perfection = perfection;
    }
}
