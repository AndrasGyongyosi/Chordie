package com.example.ChordCalculator.Model;

public class StringCatch {
    private MString string;
    private int bund;

    public StringCatch(MString string, int bund) {
        this.string = string;
        this.bund = bund;
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
}
