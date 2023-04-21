package hu.chordie.ChordCalculator.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.ChordCalculator.Model.Entities.Instrument;
import hu.chordie.ChordCalculator.Model.Entities.Rule.Rule;

import java.util.List;


@Repository
public interface RuleRepository extends CrudRepository<Rule, Integer> {
    public List<Rule> findAllByInstrument(Instrument instrument);
}
