package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.DTOs.CatchDTO;
import com.example.ChordCalculator.DTOs.CatchResultDTO;
import com.example.ChordCalculator.DTOs.StringCatchDTO;
import com.example.ChordCalculator.Model.Entities.FavoritCatch;
import com.example.ChordCalculator.Model.Entities.FavoritCatchList;
import com.example.ChordCalculator.Model.Entities.FavoritStringCatch;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.FavoritCatchListRepository;
import com.example.ChordCalculator.Model.Repositories.FavoritCatchRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/favorit")
public class FavoritController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FavoritCatchListRepository favoritCatchListRepository;

    @Autowired
    FavoritCatchRepository favoritCatchRepository;

    @RequestMapping(path="/lists/{userToken:.+}", method= RequestMethod.GET)
    public List<FavoritCatchList> getLists(@PathVariable String userToken) {
        User user = userRepository.findByUserToken(userToken);
        List<FavoritCatchList> catchLists = user.getFavoritCatchLists();
        return catchLists;
    }

    @RequestMapping(path="/newlist", method= RequestMethod.POST)
    public FavoritCatchList addNewList(@RequestBody LinkedHashMap<String, Object> params ) {
        User user = userRepository.findByUserToken((String) params.get("userToken"));

        FavoritCatchList catchList = new FavoritCatchList();
        catchList.setName((String) params.get("name"));
        catchList.setUser(user);

        favoritCatchListRepository.save(catchList);
        return catchList;
    }

    @RequestMapping(path="/addToList", method=RequestMethod.POST)
    public boolean addToList(@RequestBody LinkedHashMap<String, Object> params ){
        FavoritCatchList favoritCatchList = favoritCatchListRepository.findAllByListToken((String) params.get("listToken"));
        FavoritCatch favCatch = new FavoritCatch();
        for(HashMap<String, Object> stringCatch : (List<HashMap<String, Object>>) params.get("catch")){
            FavoritStringCatch fsc = new FavoritStringCatch();
            fsc.setFinger((int) stringCatch.get("finger"));
            fsc.setBund((int) stringCatch.get("bund"));
            fsc.setSound((String) stringCatch.get("sound"));
            fsc.setCatcha(favCatch);
            favCatch.addFavStringCatch(fsc);
        }
        favCatch.setInstrument((String) params.get("instrument"));
        favCatch.setChord((String) params.get("chord"));
        favCatch.setCatchList(favoritCatchList);
        favoritCatchList.addCatch(favCatch);

        favoritCatchListRepository.save(favoritCatchList);
        return true;
    }
    @Transactional
    @RequestMapping(path="/list/{listToken}", method = RequestMethod.DELETE)
    public void deleteList(@PathVariable String listToken){
        favoritCatchListRepository.deleteAllByListToken(listToken);
    }
    @Transactional
    @RequestMapping(path="/catch/{catchToken}", method = RequestMethod.DELETE)
    public void deleteCatch(@PathVariable String catchToken){
        favoritCatchRepository.deleteAllByCatchToken(catchToken);
    }
    private List<CatchDTO> getCatches(FavoritCatchList favoritCatchList){
    	List<CatchDTO> result = Lists.newArrayList();
        for(FavoritCatch fc : favoritCatchList.getCatches()){
            CatchDTO catcha = new CatchDTO();
            catcha.setChord(fc.getChord());
            catcha.setInstrument(fc.getInstrument());
            List<StringCatchDTO> fscList = Lists.newArrayList();
            for(FavoritStringCatch fsc : fc.getFavStringCatches()){
            	StringCatchDTO stringCatch = new StringCatchDTO();
                stringCatch.setBund(fsc.getBund());
                stringCatch.setFinger(fsc.getFinger());
                stringCatch.setSound(fsc.getSound());
                fscList.add(stringCatch);
            }
            catcha.setStringCatches(fscList);
            catcha.setToken(fc.getCatchToken());
            result.add(catcha);
        }
        return result;
    }
}
