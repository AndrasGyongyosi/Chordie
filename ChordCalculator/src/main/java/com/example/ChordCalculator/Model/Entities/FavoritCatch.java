package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Helper.RandomToken;
import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.StringCatch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Lists;

import javax.persistence.*;
import java.util.List;

@Entity
public class FavoritCatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonBackReference
    @ManyToOne
    private FavoritCatchList catchList;
    
    @JsonProperty("stringCatches")
    @OneToMany(mappedBy = "catcha", cascade = CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    private List<FavoritStringCatch> favStringCatches;

    @Column(length=2500)
    private String catchToken = RandomToken.randomString(32);

    private String instrument;

    private String chord;

    public FavoritCatch(){
        favStringCatches = Lists.newArrayList();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FavoritCatchList getCatchList() {
        return catchList;
    }

    public void setCatchList(FavoritCatchList catchList) {
        this.catchList = catchList;
    }

    public List<FavoritStringCatch> getFavStringCatches() {
        return favStringCatches;
    }
    public void addFavStringCatch(FavoritStringCatch fsc){
        this.favStringCatches.add(fsc);
    }
    public void setFavStringCatches(List<FavoritStringCatch> favStringCatches) {
        this.favStringCatches = favStringCatches;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getChord() {
        return chord;
    }

    public void setChord(String chord) {
        this.chord = chord;
    }


    public String getCatchToken() {
        return catchToken;
    }

    public void setCatchToken(String catchToken) {
        this.catchToken = catchToken;
    }
}
