package com.example.ChordCalculator.Model.Chord;


import java.util.ArrayList;
import java.util.HashMap;

public enum ChordType {
    //TODO: In complicated chords simply there is not available catch in most instrument. We need search in this way pseudo-optimal catches (f.e. 4 sound instead of 5).

    n(null,new ArrayList<String>(){{add("");add("n");}},null),
    sus4(null,new ArrayList<String>(){{add("sus4");add("sus");add("4");}},new HashMap<Integer,Integer>(){{put(1,5);}}),
    sus2(null,new ArrayList<String>(){{add("sus2");add("2");}},new HashMap<Integer,Integer>(){{put(1,2);}}),
    b5(null,new ArrayList<String>(){{add("b5");}},new HashMap<Integer,Integer>(){{put(2,6);}}),
    n6(new ArrayList<Integer>(){{add(9);}},new ArrayList<String>(){{add("6");}},null),
    n69(new ArrayList<Integer>(){{add(9);add(2);}},new ArrayList<String>(){{add("69");}},null),
    maj7(new ArrayList<Integer>(){{add(11);}},new ArrayList<String>(){{add("maj7");}},null),
    n7(new ArrayList<Integer>(){{add(10);}},new ArrayList<String>(){{add("7");}},null),
    n7b5(new ArrayList<Integer>(){{add(10);}},new ArrayList<String>(){{add("7b5");}},new HashMap<Integer,Integer>(){{put(2,6);}}),
    n7s5(new ArrayList<Integer>(){{add(10);}},new ArrayList<String>(){{add("7#5");}},new HashMap<Integer,Integer>(){{put(2,8);}}),
    n7b9(new ArrayList<Integer>(){{add(10);add(1);}},new ArrayList<String>(){{add("7b9");}},null),
    n7s9(new ArrayList<Integer>(){{add(10);add(3);}},new ArrayList<String>(){{add("7#9");}},null),
    n9(new ArrayList<Integer>(){{add(10);add(2);}},new ArrayList<String>(){{add("9");}},null),
    n9b5(new ArrayList<Integer>(){{add(10);add(2);}},new ArrayList<String>(){{add("9b5");}},new HashMap<Integer,Integer>(){{put(2,6);}}),
    n9s5(new ArrayList<Integer>(){{add(10);add(2);}},new ArrayList<String>(){{add("9#5");}},new HashMap<Integer,Integer>(){{put(2,8);}}),
    maj9(new ArrayList<Integer>(){{add(11);add(2);}},new ArrayList<String>(){{add("maj9");add("M9");}},null),
    add9(new ArrayList<Integer>(){{add(2);}},new ArrayList<String>(){{add("add9");}},null),
    n11(new ArrayList<Integer>(){{add(10);add(5);}},new ArrayList<String>(){{add("11");}},null),
    n13(new ArrayList<Integer>(){{add(10);add(9);}},new ArrayList<String>(){{add("13");}},null),
    maj13(new ArrayList<Integer>(){{add(11);add(9);}},new ArrayList<String>(){{add("maj13");}},null),
    n7sus4(new ArrayList<Integer>(){{add(10);}},new ArrayList<String>(){{add("7sus4");add("7sus");add("74");}},new HashMap<Integer,Integer>(){{put(1,5);}}),
    n9sus4(new ArrayList<Integer>(){{add(10);add(2);}},new ArrayList<String>(){{add("9sus4");add("9sus");add("94");}},new HashMap<Integer,Integer>(){{put(1,5);}});


    private ArrayList<Integer> addedNotes;
    private ArrayList<String> aliases;
    private HashMap<Integer,Integer> modifiedNotes;

    private ChordType(ArrayList<Integer> addedNotes, ArrayList<String> aliases, HashMap<Integer,Integer> modifiedNotes) {
        this.addedNotes = addedNotes;
        this.aliases = aliases;
        this.modifiedNotes = modifiedNotes;
    }

    public ArrayList<Integer> getAddedNotes() {
        return addedNotes;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }
    public HashMap<Integer, Integer> getModifiedNotes(){
        return modifiedNotes;
    }
}
