package com.kiszka.integracja.controllers;

import com.kiszka.integracja.DTOs.CommodityDTO;
import com.kiszka.integracja.entities.Conflict;
import com.kiszka.integracja.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {
    private final DataService dataService;
    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }
    @GetMapping("/commodities")
    public List<CommodityDTO> getCommoditiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return dataService.getCommoditiesByDateRange(startDate, endDate);
    }

    @GetMapping("/commodities/{commodityTypeId}")
    public List<CommodityDTO> getCommoditiesByTypeAndDateRange(
            @PathVariable int commodityTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return dataService.getCommoditiesByTypeAndDateRange(commodityTypeId, startDate, endDate);
    }
}