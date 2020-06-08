package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Model.Sound;
import com.example.ChordCalculator.Model.StringCatch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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

    private String sound;

    public StoredStringCatch(){
    	
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
