package com.example.ChordCalculator.DTOs;


import java.util.List;

import com.example.ChordCalculator.Model.CatchPerfection;

import lombok.Data;

@Data
public class InstrumentDTO {
	
	String name;
	String token;
	Boolean isPublic;
	Integer bundNumber;
	Integer maxBundDif;
	List<LabeledStringDTO> strings;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public Integer getBundNumber() {
		return bundNumber;
	}
	public void setBundNumber(Integer bundNumber) {
		this.bundNumber = bundNumber;
	}
	public Integer getMaxBundDif() {
		return maxBundDif;
	}
	public void setMaxBundDif(Integer maxBundDif) {
		this.maxBundDif = maxBundDif;
	}
	public List<LabeledStringDTO> getStrings() {
		return strings;
	}
	public void setStrings(List<LabeledStringDTO> strings) {
		this.strings = strings;
	}
	
	
}
