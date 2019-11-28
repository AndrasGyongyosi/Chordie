package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Entities.FavoritCatchList;
import com.example.ChordCalculator.Model.Entities.Instrumental;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritCatchListRepository extends CrudRepository<FavoritCatchList, Integer> {
    FavoritCatchList findAllByListToken(String token);
}
