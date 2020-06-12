package com.example.ChordCalculator.DTOs;

import lombok.Data;

@Data
public class StoredCatchDTO extends CatchDTO {

	public ChordDTO chord;
	public String instrument;
}
