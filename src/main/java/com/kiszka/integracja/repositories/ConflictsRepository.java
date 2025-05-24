package com.kiszka.integracja.repositories;


import com.kiszka.integracja.entities.Conflict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConflictsRepository extends JpaRepository<Conflict, Integer> {}