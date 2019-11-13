package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Model.StringCatch;

import javax.persistence.*;

@Entity
public class FavoritStringCatch extends StringCatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    FavoritCatch catcha;

    public FavoritStringCatch(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FavoritCatch getCatcha() {
        return catcha;
    }

    public void setCatcha(FavoritCatch catcha) {
        this.catcha = catcha;
    }
}
