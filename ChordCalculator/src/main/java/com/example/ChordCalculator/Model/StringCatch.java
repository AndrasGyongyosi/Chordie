package com.example.ChordCalculator.Model;

import com.example.ChordCalculator.Model.Entities.MString;

public class StringCatch {
    private MString string;
    private int bund;
    private int finger;
    /*finger numbers:
        1)  forefinger
        2)  middle finger
        3)  ring finger
        4)  little finger
        5)  thumb
     */
    public StringCatch(){

    }
    public StringCatch(StringCatch stringCatch){
        this.string = stringCatch.string;
        this.bund = stringCatch.bund;
        this.finger = stringCatch.finger;
    }
    public StringCatch(MString string, int bund) {
        this.string = string;
        this.bund = bund;
        //this.finger = 5;
    }
    public double getFrequency(){
        int index = string.getSound().ordinal()+bund;
        Sound sound = Sound.values()[(index)%12];
        int octave = string.getOctave() + ((index-index%12)/12);

        return sound.getBaseFrequency()*Math.pow(2,octave);
    }
    public Sound getSound(){
        return string.BundValue(bund);
    }
    public MString getString() {
        return string;
    }

    public void setString(MString string) {
        this.string = string;
    }

    public int getBund() {
        return bund;
    }

    public void setBund(int bund) {
        this.bund = bund;
    }

    public int getFinger() {
        return finger;
    }

    public void setFinger(int finger) {
        this.finger = finger;
    }
}
