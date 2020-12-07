package hu.chordie.chordCalculator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.chordCalculator.model.entities.Instrument;
import hu.chordie.chordCalculator.model.entities.StoredCatchList;

import java.util.List;

@Repository
public interface StoredCatchListRepository extends CrudRepository<StoredCatchList, Integer> {
    StoredCatchList findAllByToken(String token);
    void deleteAllByToken(String token);
}
