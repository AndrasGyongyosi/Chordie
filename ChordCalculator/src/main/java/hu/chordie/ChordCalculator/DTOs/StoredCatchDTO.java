package hu.chordie.ChordCalculator.DTOs;

import lombok.Data;

@Data
public class StoredCatchDTO extends CatchDTO {

	public ChordDTO chord;
	public String instrument;
	public String token;
}
