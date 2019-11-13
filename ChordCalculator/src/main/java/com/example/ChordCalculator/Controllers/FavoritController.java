package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.Model.Entities.FavoritCatch;
import com.example.ChordCalculator.Model.Entities.FavoritCatchList;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.FavoritCatchListRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("favorit")
public class FavoritController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FavoritCatchListRepository favoritCatchListRepository;


    @RequestMapping(path="/lists/{userToken.+}", method= RequestMethod.GET)
    public List<HashMap<String, Object>> getLists(@PathVariable String userToken) {
        List<HashMap<String, Object>> result = Lists.newArrayList();

        User user = userRepository.findByUserToken(userToken);
        List<FavoritCatchList> catchLists = user.getFavoritCatchLists();

        for (FavoritCatchList catchList : catchLists){
            result.add(new HashMap<String, Object>(){{
                put("name", catchList.getName());
                put("user", catchList.getUser().getUserToken());
                put("catches", getList(catchList.getListToken()));
            }});
        }

        return result;
    }

    @RequestMapping(path="/newList", method= RequestMethod.POST)
    public boolean addNewList(@RequestBody LinkedHashMap<String, Object> params ) {
        User user = userRepository.findByUserToken((String) params.get("userToken"));

        FavoritCatchList catchList = new FavoritCatchList();
        catchList.setName((String) params.get("name"));
        catchList.setUser(user);

        favoritCatchListRepository.save(catchList);
        return true;
    }

    @RequestMapping(path="/list/{listToken}", method = RequestMethod.GET)
    public List<HashMap<String, Object>> getList(@PathVariable String listToken){
        return null;
    }
}
