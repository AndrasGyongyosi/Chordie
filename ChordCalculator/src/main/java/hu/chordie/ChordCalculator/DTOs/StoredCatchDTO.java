package hu.chordie.chordCalculator.dtos;

import lombok.Data;

@Data
public class StoredCatchDTO extends CatchDTO {

	public ChordDTO chord;
	public String instrument;
	public String token;
}
