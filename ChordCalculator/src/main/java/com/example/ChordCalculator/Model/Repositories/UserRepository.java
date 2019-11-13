package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserToken(String userToken);
    User findByEmail(String email);
}
