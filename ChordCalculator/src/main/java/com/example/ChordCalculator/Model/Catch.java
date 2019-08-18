package com.example.ChordCalculator.Model;

import com.example.ChordCalculator.Model.Chord.Chord;

import java.util.ArrayList;
import java.util.List;

public class Catch {
    private Chord chord;
    private Instrumental instrumental;
    private List<StringCatch> stringCatches;   //.size()=instumentals strings
    private int difficulty; //1-5
    private CatchPerfection perfection;

    public Catch(Chord chord, Instrumental instrumental, List<StringCatch> stringCatches, int difficulty) {
        this.chord = chord;
        this.instrumental = instrumental;
        this.stringCatches = stringCatches;
        this.difficulty = difficulty;
    }
    public int getBundDif(){
        int maxBund = 0;
        int minBund = instrumental.getBundNumber();
        for(StringCatch sc : stringCatches){
            if(sc.getBund()>maxBund){
                maxBund = sc.getBund();
            }
            if(sc.getBund()<minBund && sc.getBund()>0){
                minBund = sc.getBund();
            }
        }

        //System.out.println("After: min "+minBund+" max "+maxBund);
        //System.out.println("Result: "+(maxBund-minBund+1));
        return maxBund-minBund+1;
    }
    public Chord getChord() {
        return chord;
    }

    public void setChord(Chord chord) {
        this.chord = chord;
    }

    public Instrumental getInstrumental() {
        return instrumental;
    }

    public void setInstrumental(Instrumental instrumental) {
        this.instrumental = instrumental;
    }

    public List<StringCatch> getStringCatches() {
        return stringCatches;
    }

    public void setStringCatches(ArrayList<StringCatch> stringCatches) {
        this.stringCatches = stringCatches;
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
