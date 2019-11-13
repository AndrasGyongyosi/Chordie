package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Helper.RandomToken;

import javax.persistence.*;
import java.util.List;

@Entity
public class FavoritCatchList{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "catchList", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<FavoritCatch> catches;

    @Column(length=2500)
    private String listToken = RandomToken.randomString(32);;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<FavoritCatch> getCatches() {
        return catches;
    }

    public void setCatches(List<FavoritCatch> catches) {
        this.catches = catches;
    }

    public String getListToken() {
        return listToken;
    }

    public void setListToken(String listToken) {
        this.listToken = listToken;
    }
}
