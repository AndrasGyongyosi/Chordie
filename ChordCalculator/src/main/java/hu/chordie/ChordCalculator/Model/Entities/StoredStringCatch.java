package hu.chordie.ChordCalculator.Model.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import hu.chordie.ChordCalculator.Model.Sound;

@Entity
public class StoredStringCatch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JsonBackReference
	private StoredCatch catcha;

	private int finger;

	private int bund;

	private Sound sound;

	public StoredStringCatch() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StoredCatch getCatcha() {
		return catcha;
	}

	public void setCatcha(StoredCatch catcha) {
		this.catcha = catcha;
	}

	public int getFinger() {
		return finger;
	}

	public void setFinger(int finger) {
		this.finger = finger;
	}

	public int getBund() {
		return bund;
	}

	public void setBund(int bund) {
		this.bund = bund;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}
}
