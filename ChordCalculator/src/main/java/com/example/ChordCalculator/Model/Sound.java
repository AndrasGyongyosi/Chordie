package com.example.ChordCalculator.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


public enum Sound {
    C("C", 0.5946, "B#"),
    CS("C#",0.6299, "Db"),
    D("D",0.6674, null),
    DS("D#",0.7071, "Eb"),
    E("E",0.7492, "Fb"),
    F("F",0.7937,"E#"),
    FS("F#",0.8409,"Gb"),
    G("G",0.8909,null),
    GS("G#",0.9439,"Ab"),
    A("A",1,null),
    AS("A#",1.0595, "Bb"),
    B("B",1.1225,"H");

    private String soundName;
    private double frequencyRate;
    private String alias;
    private static double aFrequency = 440.00;   //musical A frequency

    private Sound(String soundName, double frequencyRate, String alias) {

        this.soundName = soundName;
        this.frequencyRate = frequencyRate;
        this.alias = alias;
    }

    public String getSoundName() {
        return soundName;
    }

    public double getFrequencyRate() {
        return frequencyRate;
    }

    public double getBaseFrequency() {
        return aFrequency*frequencyRate;
    }

    public String getAlias() {
        if (alias!=null) return alias;
        else return soundName;
    }
}


