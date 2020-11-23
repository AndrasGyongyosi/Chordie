package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.DTOs.InstrumentDTO;
import com.example.ChordCalculator.DTOs.LabeledStringDTO;
import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;
import com.example.ChordCalculator.Model.Entities.StoredCatchList;
import com.example.ChordCalculator.Model.Entities.Instrument;
import com.example.ChordCalculator.Model.Entities.MString;
import com.example.ChordCalculator.Model.Entities.Rule.*;
import com.example.ChordCalculator.Repositories.InstrumentRepository;
import com.example.ChordCalculator.Repositories.MStringRepository;
import com.example.ChordCalculator.Repositories.RuleRepository;
import com.example.ChordCalculator.Repositories.UserRepository;
import com.example.ChordCalculator.Model.Entities.User;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

import com.example.ChordCalculator.Model.Sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin()
@RequestMapping("/instrument")
@Slf4j
public class InstrumentController {

    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MStringRepository mStringRepository;
    @Autowired
    RuleRepository ruleRepository;

    private static Logger logger = LoggerFactory.getLogger(InstrumentController.class);
    
    @RequestMapping(path="/{userToken:.+}", method=RequestMethod.GET)
    public List<InstrumentDTO> getInstrumentByUser(@PathVariable String userToken) {
    	logger.info("Instrument by user request");
        List<InstrumentDTO> result = Lists.newArrayList();
        User user = userRepository.findByUserToken(userToken);
        
        List<Instrument> publicInstruments = instrumentRepository.findAllByPublc(true);
        if (user != null)
            publicInstruments.addAll(user.getInstruments());
        for (Instrument instrument : publicInstruments) {
        	InstrumentDTO instrumentDTO = new InstrumentDTO();
            instrumentDTO.setName(instrument.getName());
            instrumentDTO.setInstrumentToken(instrument.getInstrumentToken());
            instrumentDTO.setBundNumber(instrument.getBundNumber());
            instrumentDTO.setIsPublic(instrument.isPublc());
            
            for (Rule rule : instrument.getRules()) {
                if (rule instanceof MaxBundDifRule) {
                    instrumentDTO.setMaxBundDif(rule.getValue());
                }
            }

            List<LabeledStringDTO> strings = Lists.newArrayList();
            for (MString actualString : instrument.getMStrings())
            {
            	LabeledStringDTO stringDTO = new LabeledStringDTO();
            	stringDTO.setName(actualString.getSound().name());
            	stringDTO.setLabel(actualString.getSound().getSoundName());
                strings.add(stringDTO);
            }
            instrumentDTO.setStrings(strings);
            
            result.add(instrumentDTO);
        }
        return result;
    }
    @RequestMapping(path="/new", method= RequestMethod.POST)
    public boolean addNewInstrumentForUser(@RequestBody InstrumentDTO dto ){
    	logger.info("Add Instrument request");
        Instrument newInstr = new Instrument();
        User owner = userRepository.findByUserToken(dto.getUserToken());
        
        List<Instrument> ownerInstruments = owner.getInstruments();
        ownerInstruments.add(newInstr);
        owner.setInstruments(ownerInstruments);

        newInstr.setUsers(Lists.newArrayList(owner));


        List<Rule> rules = new ArrayList<Rule>();
        rules.add(new MinStringsRule(newInstr, 4));
        rules.add(new MaxBundDifRule(newInstr, dto.getMaxBundDif()));
//        rules.add(new StringOrderIsConstantRule(newInstr,1));
        rules.add(new NeighborStringDifSoundRule(newInstr));

        newInstr.setRules(rules);
        newInstr.setName(dto.getName());
        newInstr.setBundNumber(dto.getBundNumber());

        instrumentRepository.save(newInstr);

        int order = 0;
        for(LabeledStringDTO mString : dto.getStrings()){
            try {
                //TODO in future : octave specialize
                MString newString = new MString(Sound.valueOf(mString.getName()), -2, newInstr, order++);
                mStringRepository.save(newString);
            } catch(InaudibleVoiceException e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @RequestMapping(path="/edit/{instrumentToken}", method= RequestMethod.POST)
    public boolean editInstrumentForUser(@RequestBody InstrumentDTO dto, @PathVariable String instrumentToken){
    	logger.info("Edit instrument request.");
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        if (instrument == null) return false;
        if (instrument.isPublc()) return false;

        for (Rule rule : ruleRepository.findAllByInstrument(instrument)){
            if (rule instanceof MaxBundDifRule)
                rule.setValue(dto.getMaxBundDif());
            ruleRepository.save(rule);
        }

        instrument.setName(dto.getName());
        instrument.setBundNumber(dto.getBundNumber());
        instrumentRepository.save(instrument);

        //not deleted
        mStringRepository.deleteAll(instrument.getMStrings());

        int order = 0;
        for(LabeledStringDTO mString : dto.getStrings()){
            try {
                //TODO in future : octave specialize
                MString newString = new MString(Sound.valueOf(mString.getName()), -2, instrument, order++);
                mStringRepository.save(newString);
            } catch(InaudibleVoiceException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return false;
            }
        }
        return true;
    }


    @RequestMapping(path="/delete/{instrumentToken}", method= RequestMethod.DELETE)
    public boolean deleteInstrument(@PathVariable String instrumentToken){
    	logger.info("Delete Instrument request");
    	
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        if (instrument == null) return false;
        if (instrument.isPublc()) return false;

        instrumentRepository.delete(instrument);

        return true;
    }

    @RequestMapping(path="/strings/{instrumentToken}")
    public List<String> getStrings(@PathVariable String instrumentToken){
    	logger.info("String by instrument request");
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        List<String> result = Lists.newArrayList();
        for(MString mString : instrument.getMStrings()){
            result.add(mString.getName());
        }
        return result;
    }
}
