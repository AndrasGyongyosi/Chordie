package com.example.ChordCalculator.DTOs;

import lombok.Data;

@Data
public class LabeledStringDTO {
	String name;
	String label;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
