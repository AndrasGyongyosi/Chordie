package com.example.ChordCalculator.DTOs;


import java.util.List;

import lombok.Data;

@Data
public class CatchResultDTO {
	
	List<CatchDTO> catches;
	int bundDif;
	String chord;
	String instrument;
	
	public List<CatchDTO> getCatches() {
		return catches;
	}
	public void setCatches(List<CatchDTO> catches) {
		this.catches = catches;
	}
	public int getBundDif() {
		return bundDif;
	}
	public void setBundDif(int bundDif) {
		this.bundDif = bundDif;
	}
	public String getChord() {
		return chord;
	}
	public void setChord(String chord) {
		this.chord = chord;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	
	
}
