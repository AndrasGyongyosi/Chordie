package com.example.ChordCalculator.Model;

import com.example.ChordCalculator.Helper.RandomToken;
import com.example.ChordCalculator.Model.Rule.Rule;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instrumental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String instrumentToken = RandomToken.randomString(32);

    public Instrumental(){
        super();
        mStrings = new ArrayList();
        users = new ArrayList();
        publc = false;
    }
    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    private List<MString> mStrings;

    @ManyToMany(mappedBy = "instrumentals")
    @JsonIgnore
    private List<User> users;

    private java.lang.String name;

    @OneToMany(mappedBy="instrument", cascade = CascadeType.ALL)
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
        this.mStrings = strings;
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
        this.rules = rules;
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

    public List<MString> getmStrings() {

        return mStrings;
    }

    public void setmStrings(List<MString> mStrings) {
        this.mStrings = mStrings;
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
