package com.example.ChordCalculator;

import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;
import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.MString;
import com.example.ChordCalculator.Model.Repositories.InstrumentRepository;
import com.example.ChordCalculator.Model.Repositories.MStringRepository;
import com.example.ChordCalculator.Model.Repositories.RuleRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.example.ChordCalculator.Model.Rule.*;
import com.example.ChordCalculator.Model.Sound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private MStringRepository mStringRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RuleRepository ruleRepository;

    @RequestMapping(value = "/")
    public String signPage() {
       // model.addAttribute("appName", "ChordCalculator");
        return "index";
    }
    /*@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/oauth_login")
    public String getLoginPage(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
                authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);

        return "oauth_login";
    }*/

    @RequestMapping(value = "/init")
    public String test() {
        mStringRepository.deleteAll();
        ruleRepository.deleteAll();
        instrumentRepository.deleteAll();
        userRepository.deleteAll();
        Instrumental guitar = new Instrumental();
        Instrumental ukulele = new Instrumental();

        List<Rule> guitarRules = new ArrayList<Rule>();
        guitarRules.add(new MinStringsRule(guitar, 4));
        guitarRules.add(new MaxBundDifRule(guitar, 4));
        guitarRules.add(new StringOrderIsConstantRule(guitar,1));
        guitarRules.add(new NeighborStringDifSoundRule(guitar));

        List<Rule> ukuleleRules = new ArrayList<Rule>();
        ukuleleRules.add(new MinStringsRule(ukulele, 4));
        ukuleleRules.add(new MaxBundDifRule(ukulele, 5));
        ukuleleRules.add(new StringOrderIsConstantRule(ukulele,1));
        ukuleleRules.add(new NeighborStringDifSoundRule(ukulele));

        guitar.setName("Acoustic guitar");
        guitar.setPublc(true);
        ukulele.setName("Ukulele");
        ukulele.setPublc(true);
        guitar.setRules(guitarRules);
        ukulele.setRules(ukuleleRules);
        guitar.setBundNumber(14);
        ukulele.setBundNumber(12    );
        //guitar.setUsers(new ArrayList(){{add(newUser);}});
        //ukulele.setUsers(new ArrayList(){{add(newUser);}});

        instrumentRepository.save(ukulele);
        instrumentRepository.save(guitar);

        try {
            //guitar
            MString actString;
            actString = new MString(Sound.E, -2, guitar,0);
            mStringRepository.save(actString);
            actString = new MString(Sound.A, -2, guitar,1);
            mStringRepository.save(actString);
            actString = new MString(Sound.D, -1, guitar,2);
            mStringRepository.save(actString);
            actString = new MString(Sound.G, -1, guitar,3);
            mStringRepository.save(actString);
            actString = new MString(Sound.B, -1, guitar,4);
            mStringRepository.save(actString);
            actString = new MString(Sound.E, 0, guitar,5);
            mStringRepository.save(actString);

            //ukulele
            actString = new MString(Sound.G, -2, ukulele,0);
            mStringRepository.save(actString);
            actString = new MString(Sound.C, -2, ukulele,1);
            mStringRepository.save(actString);
            actString = new MString(Sound.E, -2, ukulele,2);
            mStringRepository.save(actString);
            actString = new MString(Sound.A, -2, ukulele,3);
            mStringRepository.save(actString);

        } catch (
                InaudibleVoiceException e) {
            e.printStackTrace();
        }

        return "index";
    }
}
