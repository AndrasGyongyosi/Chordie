package com.example.ChordCalculator.Repositories;

import com.example.ChordCalculator.Model.Entities.MString;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MStringRepository extends CrudRepository<MString, Integer> {
}
