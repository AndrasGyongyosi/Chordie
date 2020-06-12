package com.example.ChordCalculator.DTOs;

import java.util.List;

import lombok.Data;

@Data
public class ListDTO {

	String name;
	String listToken;
	String userToken;
	List<StoredCatchDTO> catches;
}
