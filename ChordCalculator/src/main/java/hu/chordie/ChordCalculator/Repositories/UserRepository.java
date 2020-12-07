package hu.chordie.chordCalculator.repositories;

import org.springframework.data.repository.CrudRepository;

import hu.chordie.chordCalculator.model.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserToken(String userToken);
    User findByEmail(String email);
}
