package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;
import com.example.ChordCalculator.Model.Entities.Instrumental;
import com.example.ChordCalculator.Model.Entities.MString;
import com.example.ChordCalculator.Model.Entities.Rule.*;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.InstrumentRepository;
import com.example.ChordCalculator.Model.Repositories.MStringRepository;
import com.example.ChordCalculator.Model.Repositories.RuleRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.example.ChordCalculator.Model.Sound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    public List<HashMap<String, Object>> getInstrumentalsByUser(@PathVariable String userToken) {
        List result = new ArrayList();
        User user = userRepository.findByUserToken(userToken);
        List<Instrumental> publicInstruments = instrumentRepository.findAllByPublc(true);
        if (user != null)
            publicInstruments.addAll(user.getInstrumentals());
        for (Instrumental instrument : publicInstruments) {
            HashMap<String, Object> subResult = new HashMap<String, Object>();
            subResult.put("name", instrument.getName());
            subResult.put("token", instrument.getInstrumentToken());
            subResult.put("bundNumber", instrument.getBundNumber());
            subResult.put("public", instrument.isPublc());
            for (Rule rule : instrument.getRules()) {
                if (rule instanceof MaxBundDifRule) {
                    subResult.put("maxBundDif", rule.getValue());
                }
            }

            List<HashMap> strings = new ArrayList();
            for (MString actualString : instrument.getMStrings())
            {
                strings.add(new HashMap(){{put("value", actualString.getSound().name());put("name",actualString.getSound().getSoundName());}});
            }
            subResult.put("strings", strings);
            result.add(subResult);
        }
        return result;
    }
    @RequestMapping(path="/new", method= RequestMethod.POST)
    public boolean addNewInstrumentForUser(@RequestBody LinkedHashMap<String, Object> params ){
        Instrumental newInstr = new Instrumental();
        //System.out.println((String) params.get("user"));
        User owner = userRepository.findByUserToken((String) params.get("user"));
        List<Instrumental> ownerInstruments = owner.getInstrumentals();
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
                MString newString = new MString(Sound.valueOf(mString.get("value")), -2, newInstr, order++);
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
    public String editInstrumentForUser(@RequestBody LinkedHashMap<String, Object> params, @PathVariable String instrumentToken){
        Instrumental instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
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
        for(LinkedHashMap<String, String> mString : (List<LinkedHashMap<String, String>>)params.get("strings")){
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
        Instrumental instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        if (instrument == null) return "Instrument doesnt exist.";
        if (instrument.isPublc()) return "Public instrument cannot be deleted";

        instrumentRepository.delete(instrument);

        return "Instrument deleted.";
    }

    @RequestMapping(path="/strings/{instrumentToken}")
    public List<String> getStrings(@PathVariable String instrumentToken){
        Instrumental instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
        List<String> result = new ArrayList();
        for(MString mString : instrument.getMStrings()){
            result.add(mString.getName());
        }
        return result;
    }
}
