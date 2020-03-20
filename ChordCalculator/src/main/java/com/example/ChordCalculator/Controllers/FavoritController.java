package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.DTOs.CatchDTO;
import com.example.ChordCalculator.DTOs.CatchResultDTO;
import com.example.ChordCalculator.DTOs.StringCatchDTO;
import com.example.ChordCalculator.Helper.Logger;
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
    
    Logger logger = new Logger(FavoritCatchList.class);
    
    @RequestMapping(path="/lists/{userToken:.+}", method= RequestMethod.GET)
    public List<FavoritCatchList> getLists(@PathVariable String userToken) {
    	logger.info("getLists endpoint called.");
        User user = userRepository.findByUserToken(userToken);
        if (user == null) return Lists.newArrayList();
        logger.info("user email: "+user.getEmail());
        List<FavoritCatchList> catchLists = user.getFavoritCatchLists();
        return catchLists;
    }

    @RequestMapping(path="/newlistwithcatch", method= RequestMethod.POST)
    public FavoritCatchList addNewListWithCatch(@RequestBody LinkedHashMap<String, Object> params ) {
    	logger.info("addNewListWithCatch endpoint called.");
    	FavoritCatchList result = createFavoritCatchList(params);
    	FavoritCatch catcha = createFavoritCatch(params);
    	catcha.setCatchList(result);
        result.addCatch(catcha);
        favoritCatchListRepository.save(result);
        return result;
    }
    
    @RequestMapping(path="/newlist", method= RequestMethod.POST)
    public FavoritCatchList addNewList(@RequestBody LinkedHashMap<String, Object> params ) {
    	logger.info("addNewList endpoint called.");
        FavoritCatchList result = createFavoritCatchList(params);

        favoritCatchListRepository.save(result);
        return result;
    }


    @RequestMapping(path="/addToList", method=RequestMethod.POST)
    public boolean addToList(@RequestBody LinkedHashMap<String, Object> params ){
    	logger.info("addToList endpoint called.");
        FavoritCatchList favoritCatchList = favoritCatchListRepository.findAllByListToken((String) params.get("listToken"));
        FavoritCatch favCatch = createFavoritCatch(params);
        favoritCatchList.addCatch(favCatch);
        favCatch.setCatchList(favoritCatchList);
        favoritCatchListRepository.save(favoritCatchList);
        return true;
    }

    @Transactional
    @RequestMapping(path="/list/{listToken}", method = RequestMethod.DELETE)
    public void deleteList(@PathVariable String listToken){
    	logger.info("deleteList endpoint called with listToken "+listToken);
        favoritCatchListRepository.deleteAllByListToken(listToken);
    }
    @Transactional
    @RequestMapping(path="/catch/{catchToken}", method = RequestMethod.DELETE)
    public void deleteCatch(@PathVariable String catchToken){
    	logger.info("deleteCatch endpoint called with catchToken "+catchToken);
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
            result.add(catcha);
        }
        return result;
    }
    
    private FavoritCatchList createFavoritCatchList(LinkedHashMap<String, Object> params) {
		User user = userRepository.findByUserToken((String) params.get("userToken"));

        FavoritCatchList catchList = new FavoritCatchList();
        catchList.setName((String) params.get("name"));
        catchList.setUser(user);
		return catchList;
	}
    private FavoritCatch createFavoritCatch(LinkedHashMap<String, Object> params) {
		FavoritCatch favCatch = new FavoritCatch();
		System.out.println("bundNumbers:");
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
		return favCatch;
	}
}
