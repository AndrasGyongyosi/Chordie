package com.example.ChordCalculator.Model.Entities;

import com.example.ChordCalculator.Helper.RandomToken;
import com.example.ChordCalculator.Model.Catch;
import com.example.ChordCalculator.Model.Entities.MString;
import com.example.ChordCalculator.Model.Entities.Rule.Rule;
import com.example.ChordCalculator.Model.Entities.User;
import com.example.ChordCalculator.Model.StringCatch;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String instrumentToken = RandomToken.randomString(32);

    public Instrument(){
        super();
        mStrings = new ArrayList();
        users = new ArrayList();
        publc = false;
    }
    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL,fetch=FetchType.EAGER, orphanRemoval=true)
    private List<MString> mStrings;

    @ManyToMany(mappedBy = "instrumentals")
    @JsonIgnore
    private List<User> users;

    private java.lang.String name;

    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Rule> rules;
    private int bundNumber;

    public boolean isPublc() {
        return publc;
    }

    public void setPublc(boolean publc) {
        this.publc = publc;
    }

    private boolean publc;

    public List<MString> getMStrings() {
        return mStrings;
    }

    public void setMStrings(List<MString> strings) {
        this.mStrings = new ArrayList(strings);
    }
    public boolean isValid(List<StringCatch> stringCatches) throws Exception{
        //System.out.println("strings ok?");
        for (StringCatch sc : stringCatches){
            if (!mStrings.contains(sc.getString())){
                throw new Exception("Not right strings.");
            }
        }
        //System.out.println("strings ok!");
        //System.out.println("Rules: "+rules.size());
        for(Rule rule: rules){
            if (!rule.isValid(stringCatches)) return false;
        }
        return true;
    }
    public boolean isValid(Catch catcha){
        for(Rule rule : rules){
            if (!rule.isValid(catcha)) return false;
        }
        return true;
    }
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = new ArrayList(rules);
    }

    public int getBundNumber() {
        return bundNumber;
    }

    public void setBundNumber(int bundNumber) {
        this.bundNumber = bundNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getInstrumentToken() {
        return instrumentToken;
    }

    public void setInstrumentToken(String instrumentToken) {
        this.instrumentToken = instrumentToken;
    }
}
