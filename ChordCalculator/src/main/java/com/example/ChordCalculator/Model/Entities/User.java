package com.example.ChordCalculator.Model.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.Lists;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "instrumental_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "instrumental_id")

    )
    @JsonIgnore
    private List<Instrumental> instrumentals;

    @Column(length=2500)
    private String userToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<FavoritCatchList> favoritCatchLists;

    public User(String email){
        this.email = email;
        instrumentals = Lists.newArrayList();
    }
    private String email;

    public User() {
        instrumentals = Lists.newArrayList();
        favoritCatchLists = Lists.newArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Instrumental> getInstrumentals() {
        return instrumentals;
    }

    public void setInstrumentals(List<Instrumental> instrumentals) {
        this.instrumentals = instrumentals;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<FavoritCatchList> getFavoritCatchLists() {
        return favoritCatchLists;
    }

    public void setFavoritCatchLists(List<FavoritCatchList> favoritCatchLists) {
        this.favoritCatchLists = favoritCatchLists;
    }
}
