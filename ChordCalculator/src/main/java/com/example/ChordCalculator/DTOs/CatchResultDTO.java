package com.example.ChordCalculator.DTOs;


import java.util.List;

import lombok.Data;

@Data
public class CatchResultDTO {
	
	List<CatchDTO> catches;
	int bundDif;
	String chord;
	String instrument;
	int capo;
	LabeledStringDTO rootNote;
	
}
