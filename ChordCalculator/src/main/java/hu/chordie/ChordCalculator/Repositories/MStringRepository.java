package hu.chordie.ChordCalculator.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.ChordCalculator.Model.Entities.MString;

@Repository
public interface MStringRepository extends CrudRepository<MString, Integer> {
}
