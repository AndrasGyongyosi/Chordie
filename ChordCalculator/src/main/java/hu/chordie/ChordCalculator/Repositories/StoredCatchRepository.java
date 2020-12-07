package hu.chordie.chordCalculator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.chordCalculator.model.entities.StoredCatch;

@Repository
public interface StoredCatchRepository extends CrudRepository<StoredCatch, Integer> {
    void deleteAllByCatchToken(String catchToken);
}
