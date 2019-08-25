package com.example.ChordCalculator;

import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;
import com.example.ChordCalculator.Model.*;
import com.example.ChordCalculator.Model.Chord.BaseType;
import com.example.ChordCalculator.Model.Chord.Chord;
import com.example.ChordCalculator.Model.Chord.ChordType;
import com.example.ChordCalculator.Model.Repositories.InstrumentalRepository;
import com.example.ChordCalculator.Model.Repositories.MStringRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.example.ChordCalculator.Model.Rule.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@CrossOrigin()//origins = "http://localhost:3000")
public class ChordieController {

    @Autowired
    InstrumentalRepository instrumentalRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MStringRepository mStringRepository;

    //Buggy, verify is not OK
    private User getUserFromToken(String jwt_token){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(  new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("658977310896-knrl3gka66fldh83dao2rhgbblmd4un9.apps.googleusercontent.com"))
                .build();
        //System.out.println(jwt_token);
        try {
            GoogleIdToken idToken = verifier.verify(jwt_token);
            if (idToken!=null){
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                //System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                System.out.println(name);
                User user = userRepository.findByEmail(email);
                if (user==null){
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUserToken(jwt_token);
                    userRepository.save(newUser);
                    return newUser;
                }
                return user;
            }
            else{
                return null;
            }
        } catch(GeneralSecurityException|IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(path="/newUser", method= RequestMethod.POST)
    public void addNewUser(@RequestBody LinkedHashMap<String, Object> params ) {
        String jwtToken = (String) params.get("token");
        String email = (String) params.get("email");
        //TODO: need to validate token
        System.out.println(jwtToken);
        getUserFromToken(jwtToken);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserToken(jwtToken);
            userRepository.save(newUser);
        } else {
            user.setUserToken(jwtToken);
            userRepository.save(user);
        }
    }
    @RequestMapping(path = "/chordComponents/")
    public Map<String,List> getChordComponents(){
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

    @RequestMapping(path="/chordText/{text}")
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

    @RequestMapping(path = "/chord/{baseSoundString}/{baseTypeString}/{chordTypeString}")
    public ArrayList<String> getChordSoundsInString(@PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString) {
        ArrayList<String> result = new ArrayList<String>();
        for (Sound s : getChord(baseSoundString,baseTypeString,chordTypeString).getSounds()) {
            result.add(s.getSoundName());
        }
        return result;
    }

    @RequestMapping(path="/catch/{instrumentName}/{baseSoundString}/{baseTypeString}/{chordTypeString}")
    public HashMap<String, Object> getChordCatches(@PathVariable String instrumentName, @PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString) {
        HashMap<String, Object> result = new HashMap();
        //TODO: encode URI
        Instrumental instrument = instrumentalRepository.findByName(instrumentName.replace("_"," "));
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
                fingerPoints.add(stringCatchInfo);
            }

            catchInfo.put("fingerPoints",fingerPoints);
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

    @RequestMapping(path="/instrumental/{userToken:.+}")
    public List<HashMap<String, String>> getInstrumentalsByUser(@PathVariable String userToken){
        List result = new ArrayList();
        User user = userRepository.findByUserToken(userToken);
        List<Instrumental> publicInstruments = instrumentalRepository.findAllByPublc(true);
        if (user!=null)
            publicInstruments.addAll(user.getInstrumentals());
        for(Instrumental instrument : publicInstruments){
            HashMap<String,String> subResult = new HashMap<String,String>();
            subResult.put("name", instrument.getName());
            subResult.put("token", instrument.getInstrumentToken());
            result.add(subResult);
        }
        return result;
    }
    @RequestMapping(path="/newinstrument", method= RequestMethod.POST)
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
        if ((Boolean) params.get("firstSound")) {
            rules.add(new FirstSoundIsRootRule(newInstr));
        }

        newInstr.setRules(rules);
        newInstr.setName((String) params.get("instrumentalName"));
        newInstr.setBundNumber(14);




        instrumentalRepository.save(newInstr);

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
    @RequestMapping(path="/strings/{instrumentalName}")
    public List<String> getStrings(@PathVariable String instrumentalName){
        Instrumental instrument = instrumentalRepository.findByName(instrumentalName.replace("_"," "));
        List<String> result = new ArrayList();
        for(MString mString : instrument.getMStrings()){
            result.add(mString.getName());
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
}
