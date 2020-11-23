package com.example.ChordCalculator.Repositories;

import com.example.ChordCalculator.Model.Entities.StoredCatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredCatchRepository extends CrudRepository<StoredCatch, Integer> {
    void deleteAllByCatchToken(String catchToken);
}
