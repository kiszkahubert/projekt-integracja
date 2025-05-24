package com.kiszka.integracja.services;

import com.kiszka.integracja.DTOs.CommodityDTO;
import com.kiszka.integracja.DTOs.CommodityTypeDTO;
import com.kiszka.integracja.entities.CommodityType;
import com.kiszka.integracja.entities.Conflict;
import com.kiszka.integracja.entities.Commodity;
import com.kiszka.integracja.repositories.ConflictsRepository;
import com.kiszka.integracja.repositories.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {
    private final ConflictsRepository conflictsRepository;
    private final CommodityRepository commodityRepository;

    @Autowired
    public DataService(ConflictsRepository conflictsRepository, CommodityRepository commodityRepository) {
        this.conflictsRepository = conflictsRepository;
        this.commodityRepository = commodityRepository;
    }

    public List<Conflict> getAllConflicts() {
        return conflictsRepository.findAll();
    }

    public List<CommodityDTO> getCommoditiesByDateRange(LocalDate startDate, LocalDate endDate) {
        return commodityRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommodityDTO convertToDto(Commodity commodity) {
        CommodityType type = commodity.getCommodityType();
        CommodityTypeDTO typeDto = new CommodityTypeDTO(
                type.getId(),
                type.getName(),
                type.getCategory(),
                type.getQuote()
        );

        return new CommodityDTO(
                commodity.getCommodityId(),
                commodity.getDate(),
                commodity.getPrice(),
                typeDto
        );
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<CommodityDTO> getCommoditiesByTypeAndDateRange(int commodityTypeId, LocalDate startDate, LocalDate endDate) {
        return commodityRepository.findByCommodityTypeAndDateBetween(commodityTypeId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}