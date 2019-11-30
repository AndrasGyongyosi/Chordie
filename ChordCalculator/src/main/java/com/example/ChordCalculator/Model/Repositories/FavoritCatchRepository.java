package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Entities.FavoritCatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritCatchRepository extends CrudRepository<FavoritCatch, Integer> {
    void deleteAllByCatchToken(String catchToken);
}
