package hu.chordie.chordCalculator.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import hu.chordie.chordCalculator.dtos.UserDTO;
import hu.chordie.chordCalculator.exceptions.InaudibleVoiceException;
import hu.chordie.chordCalculator.model.*;
import hu.chordie.chordCalculator.model.chord.BaseType;
import hu.chordie.chordCalculator.model.chord.Chord;
import hu.chordie.chordCalculator.model.chord.ChordType;
import hu.chordie.chordCalculator.model.entities.Instrument;
import hu.chordie.chordCalculator.model.entities.MString;
import hu.chordie.chordCalculator.model.entities.StoredCatchList;
import hu.chordie.chordCalculator.model.entities.User;
import hu.chordie.chordCalculator.model.entities.rule.*;
import hu.chordie.chordCalculator.repositories.InstrumentRepository;
import hu.chordie.chordCalculator.repositories.MStringRepository;
import hu.chordie.chordCalculator.repositories.RuleRepository;
import hu.chordie.chordCalculator.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@CrossOrigin()
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MStringRepository mStringRepository;
    @Autowired
    RuleRepository ruleRepository;
    
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    //Buggy, verify is not OK
    private User getUserFromToken(String jwt_token){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(  new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("658977310896-knrl3gka66fldh83dao2rhgbblmd4un9.apps.googleusercontent.com"))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(jwt_token);
            if (idToken!=null){
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
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
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(path="/new", method= RequestMethod.POST)
    public void addNewUser(@RequestBody UserDTO dto ) {
    	logger.info("Add user request [email={}]",dto.getEmail());
    	//TODO: validate by token
        getUserFromToken(dto.getToken());
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            user = new User();
            user.setEmail(dto.getEmail());
        }
            user.setUserToken(dto.getToken());
            userRepository.save(user);
    }
}
