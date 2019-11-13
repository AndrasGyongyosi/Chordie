package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FavoritStringCatch extends StringCatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public FavoritStringCatch(){

    }
}
