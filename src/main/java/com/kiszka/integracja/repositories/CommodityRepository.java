package com.kiszka.integracja.repositories;

import com.kiszka.integracja.entities.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Integer> {
    @Query("SELECT c FROM Commodity c WHERE c.date BETWEEN :startDate AND :endDate")
    List<Commodity> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Commodity c WHERE c.commodityType.id = :commodityTypeId AND c.date BETWEEN :startDate AND :endDate")
    List<Commodity> findByCommodityTypeAndDateBetween(@Param("commodityTypeId") int commodityTypeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}