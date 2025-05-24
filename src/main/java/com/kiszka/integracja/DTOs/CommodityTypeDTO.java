package com.kiszka.integracja.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommodityTypeDTO {
    private int id;
    private String name;
    private String category;
    private String quote;
}
