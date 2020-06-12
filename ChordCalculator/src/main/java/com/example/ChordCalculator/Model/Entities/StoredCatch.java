package com.example.ChordCalculator.Model.Entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.ChordCalculator.DTOs.ChordDTO;
import com.example.ChordCalculator.Helper.RandomToken;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Lists;

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

	private ChordDTO chord;

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

	public ChordDTO getChord() {
		return chord;
	}

	public void setChord(ChordDTO chord) {
		this.chord = chord;
	}

	public String getCatchToken() {
		return catchToken;
	}

	public void setCatchToken(String catchToken) {
		this.catchToken = catchToken;
	}
}
