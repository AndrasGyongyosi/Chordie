package com.example.ChordCalculator.Model.Repositories;

import com.example.ChordCalculator.Model.Instrumental;
import com.example.ChordCalculator.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserToken(String userToken);
    User findByEmail(String email);
}
