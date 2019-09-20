package com.example.ChordCalculator.Model.Chord;

import com.example.ChordCalculator.Model.*;
import com.example.ChordCalculator.Model.CatchPerfection;

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
    public LinkedHashMap<MString,HashMap<Integer, Sound>> getPossibleFingerPoints(Instrumental instrument){
        LinkedHashMap<MString, HashMap<Integer, Sound>> possibleFingerPoints = new LinkedHashMap<>();
        List<MString> strings = instrument.getMStrings();
        Collections.sort(strings, new Comparator<MString>() {
            @Override
            public int compare(MString o1, MString o2) {
                return o2.getOrderNum().compareTo(o1.getOrderNum());
            }
        });
        for (MString mString : strings) {
            HashMap<Integer, Sound> mStringMap = new LinkedHashMap<Integer,Sound>(){{put(-1,null);}};
            for(int actualBund = 0; actualBund< instrument.getBundNumber(); actualBund++) {
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
    public void getAllCatches (List<MString> strings, LinkedHashMap<MString, HashMap<Integer, Sound>> options, Instrumental instrument, List<StringCatch> stringCatches, List<List<StringCatch>> futureResult){
        MString actualString = strings.get(0);

        for (Map.Entry<Integer, Sound> entry : options.get(actualString).entrySet()) {
            //last mString
            StringCatch stringCatch = new StringCatch(actualString, entry.getKey());
            List<StringCatch> scResult = new ArrayList<StringCatch>(stringCatches) {{
                add(stringCatch);
            }};

            if (strings.size() == 1) {
                //there is possible getting more result with the same root. But we dont need catches references to the same stringcatch.
                List<StringCatch> uniqueScResult = new ArrayList<>();
                for(StringCatch sc: scResult){
                    uniqueScResult.add(new StringCatch(sc));
                }
                futureResult.add(uniqueScResult);
            }
            //other mStrings
            else {
                try {
                    if (instrument.isValid(scResult))
                        getAllCatches(strings.subList(1, strings.size()), options, instrument, scResult, futureResult);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public List<Catch> getCatches(Instrumental instrument) {
        LinkedHashMap<MString, HashMap<Integer, Sound>> options = getPossibleFingerPoints(instrument);

        List<MString> strings = new ArrayList<MString>(options.keySet());
        //for(MString string : strings)
        //    System.out.println(string.getSound());
        List<List<StringCatch>> stringCatches = new ArrayList();

        getAllCatches(strings,options, instrument, new ArrayList(),stringCatches);

        //System.out.println("String Catches: " + stringCatches.size());
        List<Catch> catches = new ArrayList();
        for(List<StringCatch>  scs : stringCatches) {
            Catch actCatch = new Catch(this, instrument, scs, 0);
            catches.add(new Catch(this, instrument, scs, 0));
        }
        List<Catch> validatedCatches = validateCatches(catches, instrument);
        validatedCatches = addFingerNumbers(validatedCatches);

        return validatedCatches;
    }
    private List<Catch> addFingerNumbers(List<Catch> catches){
        List<Catch> result = new ArrayList<>();
        for (Catch actualCatch : catches){
            int fingerNumber = 1;
            boolean isFirst = true;
            Map<Integer, List<StringCatch>> SCHashByBund = actualCatch.getStringCatchesByBund();
            for(Map.Entry entry: SCHashByBund.entrySet()){
                List<StringCatch> scs = (List<StringCatch>)entry.getValue();
                if (scs.size()>0) {
                    //iterate backwards through the list
                    ListIterator listIterator = scs.listIterator(scs.size());
                    while(listIterator.hasPrevious()) {
                        StringCatch stringCatch = (StringCatch) listIterator.previous();
                        stringCatch.setFinger(isFirst ? fingerNumber : fingerNumber++);
                    }
                    if (isFirst) {
                        fingerNumber++;
                        isFirst = false;
                    }
                }
            }
            if (fingerNumber<=5){
                result.add(actualCatch);
            }
        }
        return result;
    }

    private List<Catch> validateCatches(List<Catch> catches, Instrumental instrument){
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

            //System.out.println(baseSound.getSoundName());
            //System.out.println(stringCatches.get(lastUsedStringIndex).getSound());
            if (usedSounds.size()==0 && instrument.isValid(catcha)){
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
