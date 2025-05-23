package com.kiszka.integracja.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity @Data
@Table(name = "commodity_price")
public class Commodity {
    @Id
    @Column(name = "price_id",nullable = false)
    private int commodityId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commodity_id", nullable = false)
    private CommodityType commodityType;
    private LocalDate date;
    private double price;
}
