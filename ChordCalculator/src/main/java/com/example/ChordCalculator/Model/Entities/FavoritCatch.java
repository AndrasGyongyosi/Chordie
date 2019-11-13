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
    private FavoritCatchList catchList;

    @OneToMany(mappedBy = "catcha", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<FavoritStringCatch> favStringCatches;

    public FavoritCatch(){

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

    public void setFavStringCatches(List<FavoritStringCatch> favStringCatches) {
        this.favStringCatches = favStringCatches;
    }
}
