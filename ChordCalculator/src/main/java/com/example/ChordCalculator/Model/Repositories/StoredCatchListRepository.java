package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Entities.StoredCatchList;
import com.example.ChordCalculator.Model.Entities.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredCatchListRepository extends CrudRepository<StoredCatchList, Integer> {
    StoredCatchList findAllByToken(String token);
    void deleteAllByToken(String token);
}
