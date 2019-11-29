package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.CatchPerfection;
import com.example.ChordCalculator.Model.Chord.BaseType;
import com.example.ChordCalculator.Model.Chord.Chord;
import com.example.ChordCalculator.Model.Chord.ChordType;
import com.example.ChordCalculator.Model.Entities.Instrumental;
import com.example.ChordCalculator.Model.Repositories.InstrumentRepository;
import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.StringCatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin()
@RequestMapping("/chord")
public class ChordController {

    @Autowired
    InstrumentRepository instrumentRepository;

    @RequestMapping(path = "/components/")
    public Map<String, List> getChordComponents(){
        Map<String, List> result = new HashMap();

        List<Map<String,String>> sounds = new ArrayList();
        for(Sound sound: Sound.values()){
            HashMap hashMap = new HashMap();
            hashMap.put("label",sound.getSoundName());
            hashMap.put("name",sound.name());
            sounds.add(hashMap);
        }
        result.put("baseSounds",sounds);

        List<Map> baseTypes = new ArrayList();
        for(BaseType baseType : BaseType.values()){
            HashMap hashMap = new HashMap();
            hashMap.put("label",baseType.getName());
            hashMap.put("name",baseType.name());
            baseTypes.add(hashMap);
        }
        result.put("baseTypes",baseTypes);

        List<Map<String,String>> chordTypes = new ArrayList();
        for(ChordType chordType : ChordType.values()){
            HashMap hashMap = new HashMap();
            hashMap.put("label",chordType.getAliases().get(0));
            hashMap.put("name",chordType.name());
            chordTypes.add(hashMap);
        }
        result.put("chordTypes",chordTypes);

        return result;
    }
    private Sound getBaseSoundFromChordText(String text){
        int resultLength = 0;
        Sound result = null;
        for(Sound sound : Sound.values()) {
            if ((text.startsWith(sound.name().toLowerCase()) || text.startsWith(sound.getSoundName().toLowerCase()) || text.startsWith(sound.getAlias().toLowerCase())) && sound.name().length()>resultLength){
                resultLength = sound.name().length();
                result = sound;
            }
        }
        return result;
    }

    @RequestMapping(path="/text/{text}")
    public HashMap<String,String> chordTextAnalyze(@PathVariable String text){
        text = text.trim().toLowerCase();
        //System.out.println("Full text: "+text);
        HashMap resultHash = new HashMap();
        HashMap<String,String> errorResult = new HashMap(){{put("error","Bad Expression");}};

        String[] parts = text.split("//");
        String chordPart = parts[0];
        //System.out.println("Chord Part: "+chordPart);
        if (parts.length>1) {
            String rootNotePart = parts[1];
        }
        Sound baseSound = getBaseSoundFromChordText(chordPart);

        if (baseSound==null) return errorResult;
        resultHash.put("bs",baseSound.name());

        String chordPartWithoutBS = chordPart.substring(baseSound.name().length());

        //System.out.println("Chord Part without BS: "+chordPartWithoutBS + " ( "+baseSound.getSoundName()+" )");

        int chordTypeLength = 0;
        ChordType passingChordType = null;
        for(ChordType ct : ChordType.values()){
            for (String alias : ct.getAliases()){
                if (chordPartWithoutBS.endsWith(alias.toLowerCase()) && chordTypeLength<=alias.length()){
                    passingChordType = ct;
                    chordTypeLength = alias.length();
                }
            }
        }
        if (passingChordType==null) return errorResult;
        resultHash.put("ct",passingChordType.name());

        String chordPartWithoutBSandCT = chordPartWithoutBS.substring(0,chordPartWithoutBS.length()-chordTypeLength);

        //System.out.println("Chord Part without BS and CT: "+chordPartWithoutBSandCT + " ( "+passingChordType.getAliases().get(0)+" )");

        int baseTypeLength = 0;
        BaseType passingBaseType = null;
        for(BaseType bt : BaseType.values()){
            for (String alias: bt.getAliases()){
                if(chordPartWithoutBSandCT.contains(alias.toLowerCase()) && baseTypeLength<=alias.length()){
                    passingBaseType = bt;
                    baseTypeLength = alias.length();
                }
            }
        }
        if (passingBaseType==null){
            return errorResult;
        }
        resultHash.put("bt",passingBaseType.name());
        //Chord resultChord = new Chord(baseSound,passingBaseType,passingChordType);


        return resultHash;

    }

    @RequestMapping(path = "type/{baseSoundString}/{baseTypeString}/{chordTypeString}")
    public ArrayList<String> getChordSoundsInString(@PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString) {
        ArrayList<String> result = new ArrayList<String>();
        for (Sound s : getChord(baseSoundString,baseTypeString,chordTypeString).getSounds()) {
            result.add(s.getSoundName());
        }
        return result;
    }

    public Chord getChord(String baseSoundString, String baseTypeString, String chordTypeString){
        Sound baseSound = null;
        BaseType baseType = null;
        ChordType chordType = null;
        try {
            for (Sound sound : Sound.values()) {
                if (sound.name().equals(baseSoundString) || sound.getSoundName().equals(baseSoundString)) {
                    baseSound = sound;
                    break;
                }
            }
            if (baseSound == null) throw new Exception("BaseSound is not exist.");

            for (BaseType bType : BaseType.values()) {
                if (bType.name().equals(baseTypeString)) {
                    baseType = bType;
                    break;
                }
            }
            if (baseType == null) throw new Exception("BaseType is not exist.");

            for (ChordType cType : ChordType.values()) {
                if (cType.name().equals(chordTypeString)){
                    chordType = cType;
                    break;
                }
                for (String names : cType.getAliases()) {
                    //System.out.println(names);
                    if (names.equals(chordTypeString)) {
                        chordType = cType;
                        break;
                    }
                }
            }
            //System.out.println(baseSound);
            //System.out.println(baseType);
            //System.out.println(chordType);
            if (chordType == null) throw new Exception("ChordType is not exist.");
            return new Chord(baseSound, baseType, chordType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(path="/catch/{instrumentToken}/{baseSoundString}/{baseTypeString}/{chordTypeString}")
    public HashMap<String, Object> getChordCatches(@PathVariable String instrumentToken, @PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString) {
        HashMap<String, Object> result = new HashMap();
        Instrumental instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        Chord chord = getChord(baseSoundString,baseTypeString,chordTypeString);
        List<Catch> catches = chord.getCatches(instrument);

        List<HashMap> catchList = new ArrayList();
        int bundDif = 0;
        for(Catch catcha: catches) {

            List<HashMap> fingerPoints = new ArrayList();
            HashMap catchInfo = new HashMap<String, Object>();
            int actualBundDif = catcha.getBundDif();
            if ( bundDif<actualBundDif) bundDif = actualBundDif;
            for (StringCatch sc : catcha.getStringCatches()) {
                HashMap stringCatchInfo = new HashMap<String, Object>();
                stringCatchInfo.put("bund",sc.getBund());
                stringCatchInfo.put("sound",(sc.getSound()==null ? null : sc.getSound().getSoundName()));
                stringCatchInfo.put("finger",sc.getFinger());
                fingerPoints.add(stringCatchInfo);
            }

            catchInfo.put("stringCatches",fingerPoints);
            catchInfo.put("perfection",catcha.getPerfection());
            catchList.add(catchInfo);
        }
        //sort catches by best to worst.
        catchList.sort(new Comparator<HashMap>() {

            @Override
            public int compare(HashMap o1, HashMap o2) {
                return -((CatchPerfection)o1.get("perfection")).getWeight().compareTo(((CatchPerfection)o2.get("perfection")).getWeight());
            }
        });

        result.put("catches", catchList);
        result.put("bundDif", bundDif);
        return result;
    }
}
