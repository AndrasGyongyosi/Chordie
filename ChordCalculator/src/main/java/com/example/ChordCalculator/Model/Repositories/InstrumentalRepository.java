package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentalRepository extends CrudRepository<Instrumental, Integer> {
    Instrumental findByUsers_email(String email);
    Instrumental findByName(String name);
    List<Instrumental> findAllByPublc(boolean publc);
}
