package hu.chordie.ChordCalculator.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.ChordCalculator.Model.Entities.Instrument;
import hu.chordie.ChordCalculator.Model.Entities.StoredCatchList;

import java.util.List;

@Repository
public interface StoredCatchListRepository extends CrudRepository<StoredCatchList, Integer> {
    StoredCatchList findAllByToken(String token);
    void deleteAllByToken(String token);
}
