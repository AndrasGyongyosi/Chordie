package hu.chordie.chordCalculator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.chordie.chordCalculator.model.entities.Instrument;

import java.util.List;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, Integer> {
    Instrument findByUsers_email(String email);
    Instrument findByInstrumentToken(String instrumentToken);
    Instrument findByName(String name);
    List<Instrument> findAllByPublc(boolean publc);
}
