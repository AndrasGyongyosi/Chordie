package hu.chordie.ChordCalculator.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.ChordCalculator.Model.Entities.StoredCatch;

@Repository
public interface StoredCatchRepository extends CrudRepository<StoredCatch, Integer> {
    void deleteAllByCatchToken(String catchToken);
}
