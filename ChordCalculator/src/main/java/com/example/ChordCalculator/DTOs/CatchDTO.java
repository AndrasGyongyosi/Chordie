package com.example.ChordCalculator.DTOs;


import java.util.List;

import com.example.ChordCalculator.Model.CatchPerfection;

import lombok.Data;

@Data
public class CatchDTO {
	
	CatchPerfection perfection;	
	List<StringCatchDTO> stringCatches;
	String chord;
	String instrument;
	String token;
	public CatchPerfection getPerfection() {
		return perfection;
	}
	public void setPerfection(CatchPerfection perfection) {
		this.perfection = perfection;
	}
	public List<StringCatchDTO> getStringCatches() {
		return stringCatches;
	}
	public void setStringCatches(List<StringCatchDTO> stringCatches) {
		this.stringCatches = stringCatches;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	

	
}
