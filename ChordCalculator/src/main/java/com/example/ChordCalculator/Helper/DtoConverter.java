package com.example.ChordCalculator.Helper;

import java.util.List;

import com.example.ChordCalculator.DTOs.LabeledStringDTO;
import com.example.ChordCalculator.DTOs.ListDTO;
import com.example.ChordCalculator.DTOs.StoredCatchDTO;
import com.example.ChordCalculator.DTOs.StringCatchDTO;
import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.Entities.StoredCatch;
import com.example.ChordCalculator.Model.Entities.StoredCatchList;
import com.example.ChordCalculator.Model.Entities.StoredStringCatch;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.Repositories.UserRepository;
import com.google.api.client.util.Lists;

public class DtoConverter {

	public static ListDTO toListDTO(StoredCatchList list) {
		ListDTO result = new ListDTO();
		List<StoredCatchDTO> catches = Lists.newArrayList();
		for (StoredCatch catcha : list.getCatches()) {
			StoredCatchDTO catchDTO = toStoredCatchDTO(catcha);
			catches.add(catchDTO);
		}
		result.setCatches(catches);
		result.setListToken(list.getToken());
		result.setName(list.getName());
		return result;
	}

	private static StoredCatchDTO toStoredCatchDTO(StoredCatch catcha) {
		StoredCatchDTO result = new StoredCatchDTO();

		result.setInstrument(catcha.getInstrument());
		result.setChord(catcha.getChord());
		List<StringCatchDTO> stringCatchList = Lists.newArrayList();

		catcha.getFavStringCatches().stream().forEach(stringCatch -> stringCatchList.add(toStringCatchDTO(stringCatch)));
		result.setStringCatches(stringCatchList);
		return result;
	}

	public static LabeledStringDTO toLabeledStringDTO(Sound sound) {
		LabeledStringDTO result = new LabeledStringDTO();
		result.setLabel(sound.getSoundName());
		result.setName(sound.name());
		return result;
	}

	public static StoredCatchList toCatchList(ListDTO dto, UserRepository userRepo) {
		User user = userRepo.findByUserToken(dto.getUserToken());

		StoredCatchList catchList = new StoredCatchList();
		catchList.setName((String) dto.getName());
		catchList.setUser(user);
		return catchList;
	}

	public static StoredCatch toCatch(StoredCatchDTO dto) {
		StoredCatch favCatch = new StoredCatch();
		for (StringCatchDTO stringCatch : dto.getStringCatches()) {
			StoredStringCatch fsc = new StoredStringCatch();
			fsc.setFinger(stringCatch.getFinger());
			fsc.setBund(stringCatch.getBund());
			fsc.setSound(stringCatch.getSound());
			fsc.setCatcha(favCatch);
			favCatch.addFavStringCatch(fsc);
		}
		favCatch.setInstrument(dto.getInstrument());
		favCatch.setChord(dto.getChord());
		return favCatch;
	}

	private static StringCatchDTO toStringCatchDTO(StoredStringCatch fsc) {
		StringCatchDTO stringCatch = new StringCatchDTO();
		stringCatch.setBund(fsc.getBund());
		stringCatch.setFinger(fsc.getFinger());
		stringCatch.setSound(fsc.getSound());
		return stringCatch;
	}
}
