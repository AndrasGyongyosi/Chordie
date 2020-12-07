package hu.chordie.chordCalculator.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Lists;

import hu.chordie.chordCalculator.helper.RandomToken;
import hu.chordie.chordCalculator.model.Sound;
import hu.chordie.chordCalculator.model.chord.BaseType;
import hu.chordie.chordCalculator.model.chord.ChordType;

@Entity
public class StoredCatch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JsonBackReference
	@ManyToOne
	private StoredCatchList catchList;

	@JsonProperty("stringCatches")
	@OneToMany(mappedBy = "catcha", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<StoredStringCatch> stringCatches;

	@Column(length = 2500)
	private String catchToken = RandomToken.randomString(32);

	private String instrument;

	private Sound baseSound;
	private BaseType baseType;
	private ChordType chordType;

	private Sound rootNote;
	private Integer capo;

	public StoredCatch() {
		stringCatches = Lists.newArrayList();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StoredCatchList getCatchList() {
		return catchList;
	}

	public void setCatchList(StoredCatchList catchList) {
		this.catchList = catchList;
	}

	public List<StoredStringCatch> getFavStringCatches() {
		return stringCatches;
	}

	public void addFavStringCatch(StoredStringCatch fsc) {
		this.stringCatches.add(fsc);
	}

	public void setFavStringCatches(List<StoredStringCatch> favStringCatches) {
		this.stringCatches = favStringCatches;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getCatchToken() {
		return catchToken;
	}

	public void setCatchToken(String catchToken) {
		this.catchToken = catchToken;
	}

	public List<StoredStringCatch> getStringCatches() {
		return stringCatches;
	}

	public void setStringCatches(List<StoredStringCatch> stringCatches) {
		this.stringCatches = stringCatches;
	}

	public Sound getBaseSound() {
		return baseSound;
	}

	public void setBaseSound(Sound baseSound) {
		this.baseSound = baseSound;
	}

	public BaseType getBaseType() {
		return baseType;
	}

	public void setBaseType(BaseType baseType) {
		this.baseType = baseType;
	}

	public ChordType getChordType() {
		return chordType;
	}

	public void setChordType(ChordType chordType) {
		this.chordType = chordType;
	}

	public Sound getRootNote() {
		return rootNote;
	}

	public void setRootNote(Sound rootNote) {
		this.rootNote = rootNote;
	}

	public Integer getCapo() {
		return capo;
	}

	public void setCapo(Integer capo) {
		this.capo = capo;
	}

}
