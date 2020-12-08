package hu.chordie.chordCalculator.dtos;

import lombok.Data;

@Data
public class ChordDTO {

	LabeledStringDTO baseSound;
	LabeledStringDTO baseType;
	LabeledStringDTO chordType;

	LabeledStringDTO rootNote;
	Integer capo;

	public ChordDTO() {
		capo = 0;
	}
}
