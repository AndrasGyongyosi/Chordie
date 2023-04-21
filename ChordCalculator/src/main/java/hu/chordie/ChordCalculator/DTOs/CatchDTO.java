package hu.chordie.ChordCalculator.DTOs;

import java.util.List;

import hu.chordie.ChordCalculator.Model.CatchPerfection;
import lombok.Data;

@Data
public class CatchDTO {

	CatchPerfection perfection;
	List<StringCatchDTO> stringCatches;
	String listToken;
}
