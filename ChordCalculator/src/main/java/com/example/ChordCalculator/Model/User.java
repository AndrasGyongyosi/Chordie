package com.example.ChordCalculator.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public User(String email){
        this.email = email;
        instrumentals = new ArrayList();
    }
    private String email;

    public User() {
        instrumentals = new ArrayList();
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
}
