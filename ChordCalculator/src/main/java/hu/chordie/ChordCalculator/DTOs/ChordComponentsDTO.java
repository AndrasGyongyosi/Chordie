package hu.chordie.ChordCalculator.DTOs;

import java.util.List;

import lombok.Data;

@Data
public class ChordComponentsDTO {
	List<LabeledStringDTO> baseSounds;
	List<LabeledStringDTO> baseTypes;	
	List<LabeledStringDTO> chordTypes;
}
