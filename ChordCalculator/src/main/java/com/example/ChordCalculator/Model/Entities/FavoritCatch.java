package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.*;
import java.util.List;

@Entity
public class FavoritCatch extends Catch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    FavoritCatchList catchList;

    @OneToMany
    List<FavoritStringCatch> stringCatches;

    public FavoritCatch(){

    }
}
