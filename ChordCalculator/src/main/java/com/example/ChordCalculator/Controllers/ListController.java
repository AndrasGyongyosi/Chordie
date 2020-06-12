package com.example.ChordCalculator.Controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.ChordCalculator.DTOs.ListDTO;
import com.example.ChordCalculator.DTOs.StoredCatchDTO;
import com.example.ChordCalculator.Helper.DtoConverter;
import com.example.ChordCalculator.Model.Entities.StoredCatch;
import com.example.ChordCalculator.Model.Entities.StoredCatchList;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.StoredCatchListRepository;
import com.example.ChordCalculator.Model.Repositories.StoredCatchRepository;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.google.api.client.util.Lists;

import lombok.extern.slf4j.Slf4j;

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

	@RequestMapping(path = "bytoken/{listToken:.+}", method = RequestMethod.GET)
	public ListDTO getListByToken(@PathVariable String listToken) {
		StoredCatchList list = catchListRepository.findAllByToken(listToken);
		if (list == null)
			return null;
		return DtoConverter.toListDTO(list);
	}

	@RequestMapping(path = "byuser/{userToken:.+}", method = RequestMethod.GET)
	public List<ListDTO> getListsByUser(@PathVariable String userToken) {
		logger.info("getLists endpoint called. [userToken={}]", userToken);
		List<ListDTO> result = Lists.newArrayList();
		User user = userRepository.findByUserToken(userToken);
		if (user == null)
			return Lists.newArrayList();
		logger.info("user email: " + user.getEmail());
		for (StoredCatchList list : user.getStoredCatchLists()) {
			result.add(DtoConverter.toListDTO(list));
		}
		return result;
	}

	@RequestMapping(path = "/new", method = RequestMethod.POST)
	public void newList(@RequestBody ListDTO dto) {
		logger.info("new catch list request. [name={}]", dto.getName());
		StoredCatchList result = DtoConverter.toCatchList(dto, userRepository);
		for (StoredCatchDTO catchDTO : dto.getCatches()) {
			StoredCatch catcha = DtoConverter.toCatch(catchDTO);
			catcha.setCatchList(result);
			result.addCatch(catcha);
		}
		catchListRepository.save(result);
	}

	@RequestMapping(path = "/addCatch", method = RequestMethod.POST)
	public boolean addToList(@RequestBody StoredCatchDTO dto) {
		logger.info("addToList endpoint called.");
		StoredCatchList storedCatchList = catchListRepository.findAllByToken(dto.getListToken());
		StoredCatch favCatch = DtoConverter.toCatch(dto);
		storedCatchList.addCatch(favCatch);
		favCatch.setCatchList(storedCatchList);
		catchListRepository.save(storedCatchList);
		return true;
	}

	@Transactional
	@RequestMapping(path = "/delete/{listToken}", method = RequestMethod.DELETE)
	public void deleteList(@PathVariable String listToken) {
		logger.info("deleteList endpoint called with listToken " + listToken);
		catchListRepository.deleteAllByToken(listToken);
	}

	@Transactional
	@RequestMapping(path = "/deleteCatch/{catchToken}", method = RequestMethod.DELETE)
	public void deleteCatch(@PathVariable String catchToken) {
		logger.info("deleteCatch endpoint called with catchToken " + catchToken);
		catchRepository.deleteAllByCatchToken(catchToken);
	}
}
