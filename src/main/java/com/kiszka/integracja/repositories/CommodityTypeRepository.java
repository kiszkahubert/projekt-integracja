package com.kiszka.integracja.repositories;

import com.kiszka.integracja.entities.CommodityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityTypeRepository extends JpaRepository<CommodityType, Integer> {}
