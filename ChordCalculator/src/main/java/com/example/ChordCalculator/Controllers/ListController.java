package com.example.ChordCalculator.Controllers;

import com.example.ChordCalculator.DTOs.CatchDTO;
import com.example.ChordCalculator.DTOs.CatchResultDTO;
import com.example.ChordCalculator.DTOs.ListDTO;
import com.example.ChordCalculator.DTOs.StringCatchDTO;
import com.example.ChordCalculator.Helper.DtoConverter;
import com.example.ChordCalculator.Model.Entities.StoredCatch;
import com.example.ChordCalculator.Model.Entities.StoredCatchList;
import com.example.ChordCalculator.Model.Entities.StoredStringCatch;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.StoredCatchListRepository;
import com.example.ChordCalculator.Model.Repositories.StoredCatchRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/list")
@Slf4j
public class ListController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StoredCatchListRepository catchListRepository;

    @Autowired
    StoredCatchRepository catchRepository;
    
    private static Logger logger = LoggerFactory.getLogger(ListController.class);
    
    @RequestMapping(path="/{userToken:.+}", method= RequestMethod.GET)
    public List<ListDTO> getLists(@PathVariable String userToken) {
    	logger.info("getLists endpoint called. [userToken={}]", userToken);
    	List<ListDTO> result = Lists.newArrayList();
        User user = userRepository.findByUserToken(userToken);
        if (user == null) return Lists.newArrayList();
        logger.info("user email: "+user.getEmail());
        for (StoredCatchList list: user.getStoredCatchLists()) {
        	result.add(DtoConverter.toListDTO(list));
        }
        return result;
    }

    @RequestMapping(path="/new", method= RequestMethod.POST)
    public void newList(@RequestBody ListDTO dto) {
    	logger.info("new catch list request. [name={}]", dto.getName());
    	StoredCatchList result = DtoConverter.toCatchList(dto, userRepository);
    	for(CatchDTO catchDTO : dto.getCatches()) {
			StoredCatch catcha = DtoConverter.toCatch(catchDTO);
			catcha.setCatchList(result);
		    result.addCatch(catcha);
    	}
        catchListRepository.save(result);
    }


    @RequestMapping(path="/addCatch", method=RequestMethod.POST)
    public boolean addToList(@RequestBody CatchDTO dto){
    	logger.info("addToList endpoint called.");
        StoredCatchList storedCatchList = catchListRepository.findAllByToken(dto.getListToken());
        StoredCatch favCatch = DtoConverter.toCatch(dto);
        storedCatchList.addCatch(favCatch);
        favCatch.setCatchList(storedCatchList);
        catchListRepository.save(storedCatchList);
        return true;
    }

    @Transactional
    @RequestMapping(path="/delete/{listToken}", method = RequestMethod.DELETE)
    public void deleteList(@PathVariable String listToken){
    	logger.info("deleteList endpoint called with listToken "+listToken);
        catchListRepository.deleteAllByToken(listToken);
    }
    @Transactional
    @RequestMapping(path="/deleteCatch/{catchToken}", method = RequestMethod.DELETE)
    public void deleteCatch(@PathVariable String catchToken){
    	logger.info("deleteCatch endpoint called with catchToken "+catchToken);
        catchRepository.deleteAllByCatchToken(catchToken);
    }
}
