package com.kiszka.integracja.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityTypeDTO {
    private int id;
    private String name;
    private String category;
    private String quote;
}
