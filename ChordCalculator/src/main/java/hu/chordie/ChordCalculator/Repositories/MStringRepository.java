package hu.chordie.chordCalculator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.chordCalculator.model.entities.MString;

@Repository
public interface MStringRepository extends CrudRepository<MString, Integer> {
}
