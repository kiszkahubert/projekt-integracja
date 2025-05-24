package com.kiszka.integracja.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity @Data
@Table(name = "commodity_type")
public class CommodityType {
    @Id
    @Column(name = "type_id")
    private int id;
    private String name;
    private String category;
    private String quote;
    @OneToMany(mappedBy = "commodityType", cascade = CascadeType.ALL)
    private List<Commodity> prices;
}
