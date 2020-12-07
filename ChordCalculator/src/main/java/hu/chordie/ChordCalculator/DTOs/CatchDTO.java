package hu.chordie.chordCalculator.dtos;

import java.util.List;

import hu.chordie.chordCalculator.model.CatchPerfection;
import lombok.Data;

@Data
public class CatchDTO {

	CatchPerfection perfection;
	List<StringCatchDTO> stringCatches;
	String listToken;
}
