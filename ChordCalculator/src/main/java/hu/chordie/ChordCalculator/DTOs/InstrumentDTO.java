package hu.chordie.ChordCalculator.DTOs;


import java.util.List;

import hu.chordie.ChordCalculator.Model.CatchPerfection;
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
