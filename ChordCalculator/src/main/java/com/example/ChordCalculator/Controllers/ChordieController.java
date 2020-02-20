package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.Exceptions.InaudibleVoiceException;
import com.example.ChordCalculator.Model.*;
import com.example.ChordCalculator.Model.Chord.BaseType;
import com.example.ChordCalculator.Model.Chord.Chord;
import com.example.ChordCalculator.Model.Chord.ChordType;
import com.example.ChordCalculator.Model.Entities.Instrument;
import com.example.ChordCalculator.Model.Entities.MString;
import com.example.ChordCalculator.Model.Entities.Rule.*;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.InstrumentRepository;
import com.example.ChordCalculator.Model.Repositories.MStringRepository;
import com.example.ChordCalculator.Model.Repositories.RuleRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.example.ChordCalculator.Model.Entities.Rule.*;
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
@CrossOrigin()
@RequestMapping("/user")
public class ChordieController {

    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MStringRepository mStringRepository;
    @Autowired
    RuleRepository ruleRepository;

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

    @RequestMapping(path="/new", method= RequestMethod.POST)
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
}
