package hu.chordie.chordCalculator.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.collect.Lists;

import hu.chordie.chordCalculator.helper.RandomToken;

import javax.persistence.*;
import java.util.List;

@Entity
public class StoredCatchList{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "catchList", cascade = CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    private List<StoredCatch> catches;

    @Column(length=2500)
    private String token = RandomToken.randomString(32);
    
    public StoredCatchList() {
    	catches = Lists.newArrayList();
    }
    
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

    public List<StoredCatch> getCatches() {
        return catches;
    }

    public void addCatch(StoredCatch fc){ this.catches.add(fc);}

    public void setCatches(List<StoredCatch> catches) {
        this.catches = catches;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String listToken) {
        this.token = listToken;
    }
}
