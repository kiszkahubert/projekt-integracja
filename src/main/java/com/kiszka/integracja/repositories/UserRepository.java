package com.kiszka.integracja.repositories;

import com.kiszka.integracja.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
