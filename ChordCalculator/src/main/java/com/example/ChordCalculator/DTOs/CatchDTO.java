package com.example.ChordCalculator.DTOs;

import java.util.List;

import com.example.ChordCalculator.Model.CatchPerfection;

import lombok.Data;

@Data
public class CatchDTO {

	CatchPerfection perfection;
	List<StringCatchDTO> stringCatches;
	public String listToken;
}
