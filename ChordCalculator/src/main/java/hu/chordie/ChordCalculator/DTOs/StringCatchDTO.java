package hu.chordie.chordCalculator.dtos;

import lombok.Data;

@Data
public class StringCatchDTO {

	int bund;
	LabeledStringDTO sound;
	int octave;
	int finger;
	int midiCode;

}
