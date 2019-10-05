package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Instrumental;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrumental, Integer> {
    Instrumental findByUsers_email(String email);
    Instrumental findByInstrumentToken(String instrumentToken);
    Instrumental findByName(String name);
    List<Instrumental> findAllByPublc(boolean publc);
}
