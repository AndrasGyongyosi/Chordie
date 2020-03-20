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
	
	public String getBaseSound() {
		return baseSound;
	}
	public void setBaseSound(String baseSound) {
		this.baseSound = baseSound;
	}
	public String getBaseType() {
		return baseType;
	}
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
	public String getChordType() {
		return chordType;
	}
	public void setChordType(String chordType) {
		this.chordType = chordType;
	}
	public String getRootNote() {
		return rootNote;
	}
	public void setRootNote(String rootNote) {
		this.rootNote = rootNote;
	}
	public Integer getCapo() {
		return capo;
	}
	public void setCapo(Integer capo) {
		this.capo = capo;
	}
	
}
