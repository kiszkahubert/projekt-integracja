package com.kiszka.integracja.services;

import com.kiszka.integracja.entities.Conflicts;
import com.kiszka.integracja.entities.Commodity;
import com.kiszka.integracja.repositories.ConflictsRepository;
import com.kiszka.integracja.repositories.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataService {
    private final ConflictsRepository conflictsRepository;
    private final CommodityRepository commodityRepository;

    @Autowired
    public DataService(ConflictsRepository conflictsRepository, CommodityRepository commodityRepository) {
        this.conflictsRepository = conflictsRepository;
        this.commodityRepository = commodityRepository;
    }

    public List<Conflicts> getAllConflicts() {
        return conflictsRepository.findAll();
    }

    public List<Commodity> getCommoditiesByDateRange(LocalDate startDate, LocalDate endDate) {
        return commodityRepository.findByDateBetween(startDate, endDate);
    }

    public List<Commodity> getCommoditiesByTypeAndDateRange(int commodityTypeId, LocalDate startDate, LocalDate endDate) {
        return commodityRepository.findByCommodityTypeAndDateBetween(commodityTypeId, startDate, endDate);
    }
}