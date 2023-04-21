package hu.chordie.ChordCalculator.Repositories;

import org.springframework.data.repository.CrudRepository;

import hu.chordie.ChordCalculator.Model.Entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserToken(String userToken);
    User findByEmail(String email);
}
