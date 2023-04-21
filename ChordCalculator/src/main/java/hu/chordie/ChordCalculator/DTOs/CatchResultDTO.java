package hu.chordie.ChordCalculator.DTOs;

import java.util.List;

import lombok.Data;

@Data
public class CatchResultDTO {

	List<CatchDTO> catches;
	int bundDif;
	String instrument;
	ChordDTO chord;
}
