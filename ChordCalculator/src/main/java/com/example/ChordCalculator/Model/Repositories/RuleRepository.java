package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Entities.Instrumental;
import com.example.ChordCalculator.Model.Entities.Rule.Rule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RuleRepository extends CrudRepository<Rule, Integer> {
    public List<Rule> findAllByInstrument(Instrumental instrument);
}
