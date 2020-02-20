package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.DTOs.InstrumentDTO;
import com.example.ChordCalculator.DTOs.LabeledStringDTO;
import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;
import com.example.ChordCalculator.Model.Entities.Instrument;
import com.example.ChordCalculator.Model.Entities.MString;
import com.example.ChordCalculator.Model.Entities.Rule.*;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.InstrumentRepository;
import com.example.ChordCalculator.Model.Repositories.MStringRepository;
import com.example.ChordCalculator.Model.Repositories.RuleRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.google.common.collect.Lists;
import com.example.ChordCalculator.Model.Sound;
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
public class InstrumentController {

    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MStringRepository mStringRepository;
    @Autowired
    RuleRepository ruleRepository;

    @RequestMapping(path="/{userToken:.+}", method=RequestMethod.GET)
    public List<InstrumentDTO> getInstrumentByUser(@PathVariable String userToken) {
        List<InstrumentDTO> result = Lists.newArrayList();
        User user = userRepository.findByUserToken(userToken);
        
        List<Instrument> publicInstruments = instrumentRepository.findAllByPublc(true);
        if (user != null)
            publicInstruments.addAll(user.getInstrumentals());
        for (Instrument instrument : publicInstruments) {
        	InstrumentDTO instrumentDTO = new InstrumentDTO();
            instrumentDTO.setName(instrument.getName());
            instrumentDTO.setToken(instrument.getInstrumentToken());
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
    public boolean addNewInstrumentForUser(@RequestBody LinkedHashMap<String, Object> params ){
        Instrument newInstr = new Instrument();
        //System.out.println((String) params.get("user"));
        User owner = userRepository.findByUserToken((String) params.get("user"));
        List<Instrument> ownerInstruments = owner.getInstrumentals();
        ownerInstruments.add(newInstr);
        owner.setInstrumentals(ownerInstruments);

        newInstr.setUsers(new ArrayList<User>(){{add(owner);}});


        List<Rule> rules = new ArrayList<Rule>();
        rules.add(new MinStringsRule(newInstr, 4));
        rules.add(new MaxBundDifRule(newInstr, Integer.parseInt((String)params.get("maxBundDif"))));
        rules.add(new StringOrderIsConstantRule(newInstr,1));
        rules.add(new NeighborStringDifSoundRule(newInstr));

        newInstr.setRules(rules);
        newInstr.setName((String) params.get("instrumentalName"));
        newInstr.setBundNumber(Integer.parseInt((String)params.get("bundNumber")));




        instrumentRepository.save(newInstr);

        int order = 0;
        for(LinkedHashMap<String, String> mString : (List<LinkedHashMap<String, String>>)params.get("strings")){
            try {
                //TODO in future : octave specialize
                MString newString = new MString(Sound.valueOf(mString.get("name")), -2, newInstr, order++);
                mStringRepository.save(newString);
            } catch(InaudibleVoiceException e){
                e.printStackTrace();
                return false;
            }
        }
        //for(Map.Entry row : params.entrySet())
        //    System.out.println("key: "+row.getKey()+", value: "+row.getValue());
        return true;
    }

    @RequestMapping(path="/edit/{instrumentToken}", method= RequestMethod.POST)
    public String editInstrumentForUser(@RequestBody Map<String, Object> params, @PathVariable String instrumentToken){
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        if (instrument == null) return "Instrument doesnt exist.";
        if (instrument.isPublc()) return "Public instrument cannot be deleted";

        for (Rule rule : ruleRepository.findAllByInstrument(instrument)){
            if (rule instanceof MaxBundDifRule)
                rule.setValue(Integer.parseInt((String)params.get("maxBundDif")));
            ruleRepository.save(rule);
        }

        instrument.setName((String) params.get("instrumentalName"));
        instrument.setBundNumber(Integer.parseInt((String)params.get("bundNumber")));
        instrumentRepository.save(instrument);

        //not deleted
        mStringRepository.deleteAll(instrument.getMStrings());

        int order = 0;
        for(Map<String, String> mString : (List<Map<String, String>>)params.get("strings")){
            try {
                //TODO in future : octave specialize
                MString newString = new MString(Sound.valueOf(mString.get("value")), -2, instrument, order++);
                mStringRepository.save(newString);
            } catch(InaudibleVoiceException e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return "Instrument edited";
    }


    @RequestMapping(path="/delete/{instrumentToken}", method= RequestMethod.DELETE)
    public String deleteInstrument(@PathVariable String instrumentToken){
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        if (instrument == null) return "Instrument doesnt exist.";
        if (instrument.isPublc()) return "Public instrument cannot be deleted";

        instrumentRepository.delete(instrument);

        return "Instrument deleted.";
    }

    @RequestMapping(path="/strings/{instrumentToken}")
    public List<String> getStrings(@PathVariable String instrumentToken){
        Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        List<String> result = new ArrayList();
        for(MString mString : instrument.getMStrings()){
            result.add(mString.getName());
        }
        return result;
    }
}
