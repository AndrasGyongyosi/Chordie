package com.example.ChordCalculator.Helper;

import java.util.List;

import com.example.ChordCalculator.DTOs.ChordDTO;
import com.example.ChordCalculator.DTOs.LabeledStringDTO;
import com.example.ChordCalculator.DTOs.ListDTO;
import com.example.ChordCalculator.DTOs.StoredCatchDTO;
import com.example.ChordCalculator.DTOs.StringCatchDTO;
import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.Chord.BaseType;
import com.example.ChordCalculator.Model.Chord.ChordType;
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
		result.setToken(catcha.getCatchToken());
		ChordDTO chord = assembleChordDTO(catcha.getBaseSound(), catcha.getBaseType(), catcha.getChordType(), catcha.getRootNote(), catcha.getCapo());
		result.setChord(chord);
		List<StringCatchDTO> stringCatchList = Lists.newArrayList();

		catcha.getFavStringCatches().stream().forEach(stringCatch -> stringCatchList.add(toStringCatchDTO(stringCatch)));
		result.setStringCatches(stringCatchList);
		return result;
	}

	public static StoredCatchList toCatchList(ListDTO dto, UserRepository userRepo) {
		User user = userRepo.findByUserToken(dto.getUserToken());

		StoredCatchList catchList = new StoredCatchList();
		catchList.setName(dto.getName());
		catchList.setUser(user);
		return catchList;
	}

	public static StoredCatch toCatch(StoredCatchDTO dto) {
		StoredCatch catchEntity = new StoredCatch();
		for (StringCatchDTO stringCatch : dto.getStringCatches()) {
			StoredStringCatch fsc = new StoredStringCatch();
			fsc.setFinger(stringCatch.getFinger());
			fsc.setBund(stringCatch.getBund());
			fsc.setSound(toSound(stringCatch.getSound()));
			fsc.setCatcha(catchEntity);
			catchEntity.addFavStringCatch(fsc);
		}
		catchEntity.setInstrument(dto.getInstrument());
		catchEntity.setBaseSound(toSound(dto.getChord().getBaseSound()));
		catchEntity.setBaseType(toBaseType(dto.getChord().getBaseType()));
		catchEntity.setChordType(toChordType(dto.getChord().getChordType()));
		catchEntity.setCapo(dto.getChord().getCapo());
		catchEntity.setRootNote(toSound(dto.getChord().getRootNote()));
		return catchEntity;
	}

	private static StringCatchDTO toStringCatchDTO(StoredStringCatch fsc) {
		StringCatchDTO stringCatch = new StringCatchDTO();
		stringCatch.setBund(fsc.getBund());
		stringCatch.setFinger(fsc.getFinger());
		stringCatch.setSound(toLabelStringDTO(fsc.getSound()));
		return stringCatch;
	}

	public static LabeledStringDTO toLabelStringDTO(Sound sound) {
		if (sound == null)
			return null;
		LabeledStringDTO res = new LabeledStringDTO();
		res.setLabel(sound.getSoundName());
		res.setName(sound.name());
		return res;
	}

	public static LabeledStringDTO toLabelStringDTO(BaseType baseType) {
		if (baseType == null)
			return null;
		LabeledStringDTO res = new LabeledStringDTO();
		res.setLabel(baseType.getName());
		res.setName(baseType.name());
		return res;
	}

	public static LabeledStringDTO toLabelStringDTO(ChordType chordType) {
		if (chordType == null)
			return null;
		LabeledStringDTO res = new LabeledStringDTO();
		res.setLabel(chordType.getAliases().get(0));
		res.setName(chordType.name());
		return res;
	}

	private static Sound toSound(LabeledStringDTO dto) {
		if (dto == null || dto.getName() == null)
			return null;
		return Sound.valueOf(dto.getName());
	}

	private static BaseType toBaseType(LabeledStringDTO dto) {
		if (dto == null || dto.getName() == null)
			return null;
		return BaseType.valueOf(dto.getName());
	}

	private static ChordType toChordType(LabeledStringDTO dto) {
		if (dto == null || dto.getName() == null)
			return null;
		return ChordType.valueOf(dto.getName());
	}

	public static ChordDTO assembleChordDTO(Sound sound, BaseType baseType, ChordType chordType, Sound rootNote, Integer capo) {
		ChordDTO res = new ChordDTO();
		res.setBaseSound(toLabelStringDTO(sound));
		res.setBaseType(toLabelStringDTO(baseType));
		res.setChordType(toLabelStringDTO(chordType));
		res.setRootNote(toLabelStringDTO(rootNote));
		res.setCapo(capo);
		return res;
	}
}
