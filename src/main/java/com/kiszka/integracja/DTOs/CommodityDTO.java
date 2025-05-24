package com.kiszka.integracja.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommodityDTO {
    private int id;
    private LocalDate date;
    private double price;
    private CommodityTypeDTO type;
}
