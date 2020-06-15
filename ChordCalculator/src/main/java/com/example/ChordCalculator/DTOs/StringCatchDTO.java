package com.example.ChordCalculator.DTOs;

import lombok.Data;

@Data
public class StringCatchDTO {

	int bund;
	LabeledStringDTO sound;
	int octave;
	int finger;
	int midiCode;

}
