package hu.chordie.chordCalculator.model.entities;

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
    @JoinTable(name = "instrument_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id")

    )
    @JsonIgnore
    private List<Instrument> instruments;

    @Column(length=2500)
    private String userToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<StoredCatchList> storedCatchLists;

    public User(String email){
        this.email = email;
        instruments = Lists.newArrayList();
    }
    private String email;

    public User() {
        instruments = Lists.newArrayList();
        storedCatchLists = Lists.newArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
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

    public List<StoredCatchList> getStoredCatchLists() {
        return storedCatchLists;
    }

    public void setStoredCatchLists(List<StoredCatchList> storedCatchLists) {
        this.storedCatchLists = storedCatchLists;
    }
}
