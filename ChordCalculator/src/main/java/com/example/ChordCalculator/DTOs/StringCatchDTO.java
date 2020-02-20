package com.example.ChordCalculator.DTOs;


import com.example.ChordCalculator.Model.Entities.MString;

import lombok.Data;

@Data
public class StringCatchDTO {
	
	int bund;
	String sound;	
	int finger;
	public int getBund() {
		return bund;
	}
	public void setBund(int bund) {
		this.bund = bund;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public int getFinger() {
		return finger;
	}
	public void setFinger(int finger) {
		this.finger = finger;
	}
	
}
