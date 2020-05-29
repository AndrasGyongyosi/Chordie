package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.DTOs.CatchDTO;
import com.example.ChordCalculator.DTOs.CatchResultDTO;
import com.example.ChordCalculator.DTOs.ChordComponentsDTO;
import com.example.ChordCalculator.DTOs.ChordDTO;
import com.example.ChordCalculator.DTOs.LabeledStringDTO;
import com.example.ChordCalculator.DTOs.StringCatchDTO;
import com.example.ChordCalculator.Exceptions.BadExpressionException;
import com.example.ChordCalculator.Helper.RandomToken;
import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.CatchPerfection;
import com.example.ChordCalculator.Model.Chord.BaseType;
import com.example.ChordCalculator.Model.Chord.Chord;
import com.example.ChordCalculator.Model.Chord.ChordType;
import com.example.ChordCalculator.Model.Entities.Instrument;
import com.example.ChordCalculator.Model.Repositories.InstrumentRepository;
import com.google.common.collect.Lists;
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
    public ChordComponentsDTO getChordComponents(){
    	ChordComponentsDTO result = new ChordComponentsDTO();
    	
        List<LabeledStringDTO> sounds = Lists.newArrayList();
        for(Sound sound: Sound.values()){
        	LabeledStringDTO soundDTO = new LabeledStringDTO();
        	soundDTO.setLabel(sound.getSoundName());
        	soundDTO.setName(sound.name());
            sounds.add(soundDTO);
        }
        result.setBaseSounds(sounds);

        List<LabeledStringDTO> baseTypes = Lists.newArrayList();
        for(BaseType baseType : BaseType.values()){
        	LabeledStringDTO baseTypeDTO = new LabeledStringDTO();
        	baseTypeDTO.setLabel(baseType.getName());
        	baseTypeDTO.setName(baseType.name());
            baseTypes.add(baseTypeDTO);
        }
        result.setBaseTypes(baseTypes);
        
        List<LabeledStringDTO> chordTypes = Lists.newArrayList();
        for(ChordType chordType : ChordType.values()){
        	LabeledStringDTO chordTypeDTO = new LabeledStringDTO();
        	chordTypeDTO.setLabel(chordType.getAliases().get(0));
        	chordTypeDTO.setName(chordType.name());
            chordTypes.add(chordTypeDTO);
        }
        result.setChordTypes(chordTypes);
        return result;
    }
    @RequestMapping(path="/text/{text}")
    public ChordDTO chordTextAnalyze(@PathVariable String text) throws BadExpressionException{
        text = text.trim().toLowerCase();
        ChordDTO result = new ChordDTO();
        
        String[] parts = text.split("<");
        text = parts[0];
        if (parts.length>1) {
            String capoPart = parts[1];
            
            //TODO: validation
            result.setCapo(Integer.parseInt(capoPart));
        }
        
        parts = text.split(">");
        String chordPart = parts[0];
        if (parts.length>1) {
            String rootNotePart = parts[1];
            result.setRootNote(getSoundFromChordText(rootNotePart).name());
            
        }
        Sound baseSound = getSoundFromChordText(chordPart);
        	
        result.setBaseSound(baseSound.name());

        String chordPartWithoutBS = chordPart.substring(baseSound.name().length());

        int chordTypeLength = 0;
        ChordType chordType = null;
        for(ChordType ct : ChordType.values()){
            for (String alias : ct.getAliases()){
                if (chordPartWithoutBS.endsWith(alias.toLowerCase()) && chordTypeLength<=alias.length()){
                    chordType = ct;
                    chordTypeLength = alias.length();
                }
            }
        }
        result.setChordType(chordType.name());

        String chordPartWithoutBSandCT = chordPartWithoutBS.substring(0,chordPartWithoutBS.length()-chordTypeLength);

        int baseTypeLength = 0;
        BaseType baseType = null;
        for(BaseType bt : BaseType.values()){
            for (String alias: bt.getAliases()){
                if(chordPartWithoutBSandCT.contains(alias.toLowerCase()) && baseTypeLength<=alias.length()){
                    baseType = bt;
                    baseTypeLength = alias.length();
                }
            }
        }
        if (baseSound==null || chordType==null || baseType==null) {
        	throw new BadExpressionException("Wrong free text chord: "+text);
        }
        result.setBaseType(baseType.name());

        return result;

    }

    @RequestMapping(path="/catch/{instrumentToken}/{baseSoundString}/{baseTypeString}/{chordTypeString}/{rootNoteString}/{capo}")
    public CatchResultDTO getChordCatches(@PathVariable String instrumentToken, @PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString, @PathVariable String rootNoteString, @PathVariable Integer capo) throws BadExpressionException{
    	CatchResultDTO result = new CatchResultDTO();
    	
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        Chord chord = getChord(baseSoundString,baseTypeString,chordTypeString);
        
        
        Sound rootNote = null;
        for (Sound sound : Sound.values()) {
            if (sound.name().equalsIgnoreCase(rootNoteString))
                rootNote = sound;
        }
        
        List<Catch> catches = chord.getCatches(instrument.getInstrumentWithCapo(capo), rootNote);

        List<CatchDTO> catchList = Lists.newArrayList();
        
        int bundDif = 0;
        for(Catch catcha: catches) {
            CatchDTO catchInfo = new CatchDTO();
            
            List<StringCatchDTO> fingerPoints = Lists.newArrayList();
            int actualBundDif = catcha.getBundDif();
            
            if ( bundDif<actualBundDif) bundDif = actualBundDif;
            
            for (StringCatch stringCatch : catcha.getStringCatches()) {
            	StringCatchDTO stringCatchDTO = new StringCatchDTO();
            	Sound sound = stringCatch.getSound();
                stringCatchDTO.setBund(stringCatch.getBund()<0 ? stringCatch.getBund(): stringCatch.getBund()+capo);
                stringCatchDTO.setFinger(stringCatch.getFinger());
                
                if (sound!=null) {
                	stringCatchDTO.setSound(sound.getSoundName());
                	int octave = stringCatch.getString().getOctave(stringCatch.getBund()+capo);
					stringCatchDTO.setMidiCode(sound.getMIDICodeAtOctave(octave));
                }
                
                fingerPoints.add(stringCatchDTO);
            }

            catchInfo.setStringCatches(fingerPoints);
            catchInfo.setPerfection(catcha.getPerfection());
            catchList.add(catchInfo);
        }
        //sort catches by best to worst.
        catchList.sort(new Comparator<CatchDTO>() {

            @Override
            public int compare(CatchDTO c1, CatchDTO c2) {
                return -((CatchPerfection)c1.getPerfection()).getWeight().compareTo(((CatchPerfection)c2.getPerfection()).getWeight());
            }
        });

        result.setCatches(catchList);
        result.setBundDif(bundDif);
        result.setChord(chord.getFullName());
        result.setCapo(capo);
        result.setRootNote(rootNoteString);
        return result;
    }
    
    private Sound getSoundFromChordText(String text){
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
    private Chord getChord(String baseSoundString, String baseTypeString, String chordTypeString) throws BadExpressionException{
        Sound baseSound = null;
        BaseType baseType = null;
        ChordType chordType = null;
        for (Sound sound : Sound.values()) {
            if (sound.name().equals(baseSoundString) || sound.getSoundName().equals(baseSoundString)) {
                baseSound = sound;
                break;
            }
        }
        if (baseSound == null) throw new BadExpressionException("BaseSound is not exist: "+baseSoundString);

        for (BaseType bType : BaseType.values()) {
            if (bType.name().equals(baseTypeString)) {
                baseType = bType;
                break;
            }
        }
        if (baseType == null) throw new BadExpressionException("BaseType is not exist: "+baseTypeString);

        for (ChordType cType : ChordType.values()) {
            if (cType.name().equals(chordTypeString)){
                chordType = cType;
                break;
            }
            for (String names : cType.getAliases()) {
                if (names.equals(chordTypeString)) {
                    chordType = cType;
                    break;
                }
            }
        }
        if (chordType == null) throw new BadExpressionException("ChordType is not exist: "+chordTypeString);
        return new Chord(baseSound, baseType, chordType);
    }
    
}
