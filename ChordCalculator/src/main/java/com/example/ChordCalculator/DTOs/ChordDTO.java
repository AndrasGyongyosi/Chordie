package com.example.ChordCalculator.DTOs;


import lombok.Data;

@Data
public class ChordDTO {
	
	String baseSound;
	String baseType;	
	String chordType;
	
	String rootNote;
	Integer capo;

	public ChordDTO() {
		capo = 0;
	}
}
