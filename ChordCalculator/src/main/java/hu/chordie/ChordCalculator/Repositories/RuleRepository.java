package hu.chordie.chordCalculator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.chordCalculator.model.entities.Instrument;
import hu.chordie.chordCalculator.model.entities.rule.Rule;

import java.util.List;


@Repository
public interface RuleRepository extends CrudRepository<Rule, Integer> {
    public List<Rule> findAllByInstrument(Instrument instrument);
}
