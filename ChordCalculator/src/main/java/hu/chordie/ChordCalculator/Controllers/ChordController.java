package hu.chordie.ChordCalculator.Controllers;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import hu.chordie.ChordCalculator.DTOs.CatchDTO;
import hu.chordie.ChordCalculator.DTOs.CatchResultDTO;
import hu.chordie.ChordCalculator.DTOs.ChordComponentsDTO;
import hu.chordie.ChordCalculator.DTOs.ChordDTO;
import hu.chordie.ChordCalculator.DTOs.LabeledStringDTO;
import hu.chordie.ChordCalculator.DTOs.StringCatchDTO;
import hu.chordie.ChordCalculator.Exceptions.BadExpressionException;
import hu.chordie.ChordCalculator.Helper.DtoConverter;
import hu.chordie.ChordCalculator.Model.Catch;
import hu.chordie.ChordCalculator.Model.CatchPerfection;
import hu.chordie.ChordCalculator.Model.Sound;
import hu.chordie.ChordCalculator.Model.StringCatch;
import hu.chordie.ChordCalculator.Model.Chord.BaseType;
import hu.chordie.ChordCalculator.Model.Chord.Chord;
import hu.chordie.ChordCalculator.Model.Chord.ChordType;
import hu.chordie.ChordCalculator.Model.Entities.Instrument;
import hu.chordie.ChordCalculator.Repositories.InstrumentRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin()
@RequestMapping("/chord")
@Slf4j
public class ChordController {

	@Autowired
	InstrumentRepository instrumentRepository;
	private static Logger logger = LoggerFactory.getLogger(ChordController.class);

	@RequestMapping(path = "/components/")
	public ChordComponentsDTO getChordComponents() {
		logger.info("Chord components list request.");
		ChordComponentsDTO result = new ChordComponentsDTO();

		List<LabeledStringDTO> sounds = Lists.newArrayList();
		Lists.newArrayList(Sound.values()).stream().forEach(sound -> sounds.add(DtoConverter.toLabelStringDTO(sound)));
		result.setBaseSounds(sounds);

		List<LabeledStringDTO> baseTypes = Lists.newArrayList();
		Lists.newArrayList(BaseType.values()).stream().forEach(baseType -> baseTypes.add(DtoConverter.toLabelStringDTO(baseType)));
		result.setBaseTypes(baseTypes);

		List<LabeledStringDTO> chordTypes = Lists.newArrayList();
		Lists.newArrayList(ChordType.values()).stream().forEach(chordType -> chordTypes.add(DtoConverter.toLabelStringDTO(chordType)));
		result.setChordTypes(chordTypes);

		return result;
	}

	@RequestMapping(path = "/text/{chordString}")
	public ChordDTO chordTextAnalyze(@PathVariable String chordString) throws BadExpressionException {
		logger.info("Chord components request by text. [ text=" + chordString + "]");
		chordString = chordString.trim().toLowerCase();
		ChordDTO result = new ChordDTO();
		try {
			String[] parts = chordString.split("<");
			chordString = parts[0];
			if (parts.length > 1) {
				String capoPart = parts[1];

				// TODO: validation
				result.setCapo(Integer.parseInt(capoPart));
			}

			parts = chordString.split(">");
			chordString = parts[0];
			if (parts.length > 1) {
				String rootNotePart = parts[1];
				result.setRootNote(DtoConverter.toLabelStringDTO(getSoundFromChordText(rootNotePart)));

			}
			Sound baseSound = getSoundFromChordText(chordString);

			result.setBaseSound(DtoConverter.toLabelStringDTO(baseSound));

			chordString = chordString.substring(baseSound.name().length());

			int chordTypeLength = 0;
			ChordType chordType = null;
			for (ChordType ct : ChordType.values()) {
				for (String alias : ct.getAliases()) {
					if (chordString.endsWith(alias.toLowerCase()) && chordTypeLength <= alias.length()) {
						chordType = ct;
						chordTypeLength = alias.length();
					}
				}
			}
			result.setChordType(DtoConverter.toLabelStringDTO(chordType));

			chordString = chordString.substring(0, chordString.length() - chordTypeLength);

			int baseTypeLength = 0;
			BaseType baseType = null;
			for (BaseType bt : BaseType.values()) {
				for (String alias : bt.getAliases()) {
					if (chordString.contains(alias.toLowerCase()) && baseTypeLength <= alias.length()) {
						baseType = bt;
						baseTypeLength = alias.length();
					}
				}
			}
			result.setBaseType(DtoConverter.toLabelStringDTO(baseType));

			if (baseSound == null || chordType == null || baseType == null) {
				throw new BadExpressionException("Wrong free text chord: " + chordString);
			}
		} catch (Exception e) {
			throw new BadExpressionException(e.getMessage());
		}

		return result;

	}

	@RequestMapping(path = "/catch/{instrumentToken}/{baseSoundString}/{baseTypeString}/{chordTypeString}/{rootNoteString}/{capo}")
	public CatchResultDTO getChordCatches(@PathVariable String instrumentToken, @PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString, @PathVariable String rootNoteString, @PathVariable Integer capo) throws BadExpressionException {
		logger.info("Chord catches request.");

		CatchResultDTO result = new CatchResultDTO();

		Instrument instrument = instrumentRepository.findByInstrumentToken(instrumentToken);
		Chord chord = getChord(baseSoundString, baseTypeString, chordTypeString);

		Sound rootNote = null;
		for (Sound sound : Sound.values()) {
			if (sound.name().equalsIgnoreCase(rootNoteString))
				rootNote = sound;
		}

		List<Catch> catches = chord.getCatches(instrument.getInstrumentWithCapo(capo), rootNote);

		List<CatchDTO> catchList = Lists.newArrayList();

		int bundDif = 0;
		for (Catch catcha : catches) {
			CatchDTO catchInfo = new CatchDTO();

			List<StringCatchDTO> fingerPoints = Lists.newArrayList();
			int actualBundDif = catcha.getBundDif();

			if (bundDif < actualBundDif)
				bundDif = actualBundDif;

			for (StringCatch stringCatch : catcha.getStringCatches()) {
				StringCatchDTO stringCatchDTO = new StringCatchDTO();
				Sound sound = stringCatch.getSound();
				stringCatchDTO.setBund(stringCatch.getBund() < 0 ? stringCatch.getBund() : stringCatch.getBund() + capo);
				stringCatchDTO.setFinger(stringCatch.getFinger());

				if (sound != null) {
					stringCatchDTO.setSound(DtoConverter.toLabelStringDTO(sound));
					int octave = stringCatch.getString().getOctave(stringCatch.getBund() + capo);
					stringCatchDTO.setOctave(octave);
					stringCatchDTO.setMidiCode(sound.getMIDICodeAtOctave(octave));
				}
				fingerPoints.add(stringCatchDTO);
			}

			catchInfo.setStringCatches(fingerPoints);
			catchInfo.setPerfection(catcha.getPerfection());
			catchList.add(catchInfo);
		}
		// sort catches by best to worst.
		catchList.sort(new Comparator<CatchDTO>() {

			@Override
			public int compare(CatchDTO c1, CatchDTO c2) {
				return -((CatchPerfection) c1.getPerfection()).getWeight().compareTo(((CatchPerfection) c2.getPerfection()).getWeight());
			}
		});

		result.setCatches(catchList);
		result.setBundDif(bundDif);
		result.setChord(DtoConverter.assembleChordDTO(chord.getBaseSound(), chord.getBaseType(), chord.getChordType(), rootNote, capo));
		return result;
	}

	@RequestMapping(path = "/sound/{baseSoundString}/{baseTypeString}/{chordTypeString}")
	public List<LabeledStringDTO> getSoundsByChordComponents(@PathVariable String baseSoundString, @PathVariable String baseTypeString, @PathVariable String chordTypeString) throws BadExpressionException {
		logger.info("Sounds by chord components request.");
		Chord chord = getChord(baseSoundString, baseTypeString, chordTypeString);
		List<LabeledStringDTO> result = Lists.newArrayList();
		for (Sound sound : chord.getSounds()) {
			LabeledStringDTO soundDTO = new LabeledStringDTO();
			soundDTO.setLabel(sound.getSoundName());
			soundDTO.setName(sound.name());
			result.add(soundDTO);
		}
		return result;
	}

	private Sound getSoundFromChordText(String text) {
		int resultLength = 0;
		Sound result = null;
		for (Sound sound : Sound.values()) {
			if ((text.startsWith(sound.name().toLowerCase()) || text.startsWith(sound.getSoundName().toLowerCase()) || text.startsWith(sound.getAlias().toLowerCase())) && sound.name().length() > resultLength) {
				resultLength = sound.name().length();
				result = sound;
			}
		}
		return result;
	}

	private Chord getChord(String baseSoundString, String baseTypeString, String chordTypeString) throws BadExpressionException {
		Sound baseSound = null;
		BaseType baseType = null;
		ChordType chordType = null;
		for (Sound sound : Sound.values()) {
			if (sound.name().equals(baseSoundString) || sound.getSoundName().equals(baseSoundString)) {
				baseSound = sound;
				break;
			}
		}
		if (baseSound == null)
			throw new BadExpressionException("BaseSound is not exist: " + baseSoundString);

		for (BaseType bType : BaseType.values()) {
			if (bType.name().equals(baseTypeString)) {
				baseType = bType;
				break;
			}
		}
		if (baseType == null)
			throw new BadExpressionException("BaseType is not exist: " + baseTypeString);

		for (ChordType cType : ChordType.values()) {
			if (cType.name().equals(chordTypeString)) {
				chordType = cType;
				break;
			}
			for (String names : cType.getAliases()) {
				if (names.equals(chordTypeString)) {
					chordType = cType;
					break;
				}
			}
		}
		if (chordType == null)
			throw new BadExpressionException("ChordType is not exist: " + chordTypeString);
		return new Chord(baseSound, baseType, chordType);
	}

}
