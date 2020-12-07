package hu.chordie.chordCalculator.dtos;


import java.util.List;

import hu.chordie.chordCalculator.model.CatchPerfection;
import lombok.Data;

@Data
public class InstrumentDTO {
	
	String name;
	String instrumentToken;
	
	Boolean isPublic;
	Integer bundNumber;
	Integer maxBundDif;
	String userToken;
	
	List<LabeledStringDTO> strings;
}
