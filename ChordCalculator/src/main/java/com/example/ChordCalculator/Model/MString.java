package com.example.ChordCalculator.Model;

import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class MString {

    private Sound sound;
    private int octave;

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    private Integer orderNum;
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Instrumental instrument;

    public MString(){
        super();
    }
    public MString(Sound s, int o, Instrumental instrument, int order) throws InaudibleVoiceException{
        this(s,o, instrument);
        this.orderNum=order;
    }
    public MString(Sound s, int o, Instrumental instrument)throws InaudibleVoiceException{
        this.instrument = instrument;
        if (Math.abs(o)<4){
            octave = o;
        }
        else{
            throw new InaudibleVoiceException("Too high or too short voice, human ear cannot hear it.");
        }
        this.orderNum = instrument.getMStrings().size();
        sound = s;
    }
    public ArrayList<Integer> getBundNumberBySound(Sound searchedSound){
        ArrayList<Integer> result = new ArrayList<>();
        int actualBund = (searchedSound.ordinal()-sound.ordinal());
        if (actualBund<0) actualBund+=12;

        while (actualBund< instrument.getBundNumber()){
            result.add(actualBund);
            actualBund+=12;
        }
        return result;
    }

    public Sound BundValue(int bund){
        if (bund==-1) return null;
        return Sound.values()[(sound.ordinal()+bund)%12];
    }
    public String getName(){
        return sound.getSoundName() + " " + octave;
    }
    public double getFrequency(){
        return sound.getBaseFrequency()*Math.pow(2,octave);
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public Instrumental getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrumental instrument) {
        this.instrument = instrument;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PreRemove
    private void beforeRemove(){
        instrument = null;
    }
}
