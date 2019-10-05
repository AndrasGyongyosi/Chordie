package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.MString;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MStringRepository extends CrudRepository<MString, Integer> {
    public void deleteAllByInstrument(Instrumental instrumental);
}
