package com.example.ChordCalculator.Model.Chord;

import com.example.ChordCalculator.Exceptions.NotRegularChordException;
import com.example.ChordCalculator.Model.*;
import com.example.ChordCalculator.Model.Rule.CatchPerfection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.*;

@Entity
public class Chord {
    @Id
    @GeneratedValue
    private int id;

    private ArrayList<Sound> sounds;
    private ChordType chordType;
    private BaseType baseType;
    private Sound baseSound;

    public Chord(Sound sound, BaseType bt, ChordType ct){
        baseSound = sound;
        baseType = bt;
        chordType = ct;

        ArrayList<Integer> neededNotes = new ArrayList(bt.getStructure());
        if (ct.getModifiedNotes()!=null) {
            for (HashMap.Entry<Integer, Integer> entry : ct.getModifiedNotes().entrySet()) {
                neededNotes.set(entry.getKey(), entry.getValue());
            }
        }
        if(ct.getAddedNotes()!=null) {
            for (Integer note : ct.getAddedNotes()) {
                neededNotes.add(note);
            }
        }
        int rootNoteNumber = 0;
        for(Sound s :Sound.values()){
            if (s.equals(sound)){
                rootNoteNumber = s.ordinal();
                break;
            }
        }
        sounds = new ArrayList<Sound>();
        for (Integer note: neededNotes){
            sounds.add(Sound.values()[(rootNoteNumber + note)%12]);
        }
    }
    public LinkedHashMap<MString,HashMap<Integer, Sound>> getPossibleFingerPoints(Instrumental instrumental){
        LinkedHashMap<MString, HashMap<Integer, Sound>> possibleFingerPoints = new LinkedHashMap<>();
        List<MString> strings = instrumental.getMStrings();
        Collections.sort(strings, new Comparator<MString>() {
            @Override
            public int compare(MString o1, MString o2) {
                return o2.getOrderNum().compareTo(o1.getOrderNum());
            }
        });
        for (MString mString : strings) {
            HashMap<Integer, Sound> mStringMap = new LinkedHashMap<Integer,Sound>(){{put(-1,null);}};
            for(int actualBund=0; actualBund<instrumental.getBundNumber();actualBund++) {
                for (Sound actualSound : sounds) {
                    if (mString.BundValue(actualBund).equals(actualSound)){
                        mStringMap.put(actualBund,actualSound);
                    }
                }
            }
            possibleFingerPoints.put(mString, mStringMap);

        }
        return possibleFingerPoints;
    }
    //recursive!
    public void getAllCatches (List<MString> strings, LinkedHashMap<MString, HashMap<Integer, Sound>> options, Instrumental instrumental, List<StringCatch> stringCatches, List<List<StringCatch>> futureResult){
        MString actualString = strings.get(0);
        List<List<StringCatch>> result;
        //System.out.println("Remaining Strings: "+strings.size());
        for (Map.Entry<Integer, Sound> entry : options.get(actualString).entrySet()) {
            //last mString
            StringCatch stringCatch = new StringCatch(actualString, entry.getKey());
            List<StringCatch> scResult = new ArrayList<StringCatch>(stringCatches) {{
                add(stringCatch);
            }};
            //System.out.println("Actual catch strings: "+scResult.size());
            if (strings.size() == 1) {
                futureResult.add(scResult);
                //System.out.println("result find.");
            }
            //other mStrings
            else {
                try {
                    if (instrumental.isValid(scResult))
                        getAllCatches(strings.subList(1, strings.size()), options, instrumental, scResult, futureResult);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public List<Catch> getCatches(Instrumental instrumental) {
        LinkedHashMap<MString, HashMap<Integer, Sound>> options = getPossibleFingerPoints(instrumental);

        List<MString> strings = new ArrayList<MString>(options.keySet());
        //for(MString string : strings)
        //    System.out.println(string.getSound());
        List<List<StringCatch>> stringCatches = new ArrayList();

        getAllCatches(strings,options,instrumental, new ArrayList(),stringCatches);

        //System.out.println("String Catches: " + stringCatches.size());
        List<Catch> catches = new ArrayList();
        for(List<StringCatch>  scs : stringCatches) {
            Catch actCatch = new Catch(this, instrumental, scs, 0);
            catches.add(new Catch(this, instrumental, scs, 0));
        }
        //System.out.println("Catches: "+catches.size());
        List<Catch> validatedCatches = validateCatches(catches, instrumental);
        //System.out.println("ValidatedCatches: "+validatedCatches.size());
        return validatedCatches;
    }

    public List<Catch> validateCatches(List<Catch> catches, Instrumental instrumental){
        List<Catch> result = new ArrayList();
        for(Catch catcha : catches){
            List<Sound> usedSounds = new ArrayList(sounds);
            List<StringCatch> stringCatches = catcha.getStringCatches();

            int lastUsedStringIndex = -1;
            for(StringCatch sc : stringCatches){
                Sound sound = sc.getSound();
                if (sound!=null)
                    usedSounds.remove(sc.getSound());
                if(sc.getBund()!=-1){
                    lastUsedStringIndex++;
                }
            }

            System.out.println(baseSound.getSoundName());
            //System.out.println(stringCatches.get(lastUsedStringIndex).getSound());
            if (usedSounds.size()==0 && instrumental.isValid(catcha)){
                if (stringCatches.get(lastUsedStringIndex).getSound().equals(baseSound))
                    catcha.setPerfection(CatchPerfection.HIGH);
                else
                    catcha.setPerfection(CatchPerfection.MEDIUM);
                result.add(catcha);
            }
        }
        return result;
    }

    public String getFullName(){
        return baseSound.getSoundName()+" "+baseType.getName()+" "+chordType.getAliases().get(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public ChordType getChordType() {
        return chordType;
    }

    public void setChordType(ChordType chordType) {
        this.chordType = chordType;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public Sound getBaseSound() {
        return baseSound;
    }

    public void setBaseSound(Sound baseSound) {
        this.baseSound = baseSound;
    }
}
