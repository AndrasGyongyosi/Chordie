package com.example.ChordCalculator.DTOs;


import java.util.List;

import com.example.ChordCalculator.Model.CatchPerfection;

import lombok.Data;

@Data
public class ListDTO {
	
	String name;
	String listToken;
	String userToken;
	List<CatchDTO> catches;
}
