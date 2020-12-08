package hu.chordie.chordCalculator.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


public enum Sound {
    C("C", 0.5946, "B#",0),
    CS("C#",0.6299, "Db",1),
    D("D",0.6674, null,2),
    DS("D#",0.7071, "Eb",3),
    E("E",0.7492, "Fb",4),
    F("F",0.7937,"E#",5),
    FS("F#",0.8409,"Gb",6),
    G("G",0.8909,null,7),
    GS("G#",0.9439,"Ab",8),
    A("A",1,null,9),
    AS("A#",1.0595, "Bb",10),
    B("B",1.1225,"H",11);

    private String soundName;
    private double frequencyRate;
    private String alias;
    private Integer MIDICode;
    private static double aFrequency = 440.00;   //musical A frequency

    private Sound(String soundName, double frequencyRate, String alias, Integer MIDICode) {

        this.soundName = soundName;
        this.frequencyRate = frequencyRate;
        this.alias = alias;
        this.MIDICode = MIDICode;
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

	public Integer getMIDICode() {
		return MIDICode;
	}
	
	public Integer getMIDICodeAtOctave(Integer octave) {
		return (octave+5)*12 + MIDICode;
	}
	
	public Sound transpone(Integer capo) {
		return Sound.values()[(ordinal()+capo)%12];
	}
   
}


