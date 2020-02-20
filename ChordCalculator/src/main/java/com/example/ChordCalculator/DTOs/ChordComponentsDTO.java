package com.example.ChordCalculator.DTOs;

import java.util.List;

import lombok.Data;

@Data
public class ChordComponentsDTO {
	List<LabeledStringDTO> baseSounds;
	List<LabeledStringDTO> baseTypes;	
	List<LabeledStringDTO> chordTypes;
	public List<LabeledStringDTO> getBaseSounds() {
		return baseSounds;
	}
	public void setBaseSounds(List<LabeledStringDTO> baseSoundDtos) {
		this.baseSounds = baseSoundDtos;
	}
	public List<LabeledStringDTO> getBaseTypes() {
		return baseTypes;
	}
	public void setBaseTypes(List<LabeledStringDTO> baseTypeDtos) {
		this.baseTypes = baseTypeDtos;
	}
	public List<LabeledStringDTO> getChordTypes() {
		return chordTypes;
	}
	public void setChordTypes(List<LabeledStringDTO> chordTypeDtos) {
		this.chordTypes = chordTypeDtos;
	}
}
