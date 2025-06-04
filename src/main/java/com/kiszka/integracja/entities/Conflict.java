package com.kiszka.integracja.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity @Data
@Table(name = "conflicts")
public class Conflict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conflict_id")
    private int id;
    private String name;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
}
