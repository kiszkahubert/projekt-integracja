package com.kiszka.integracja.soap;

import com.kiszka.integracja.entities.Conflict;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Conflict", propOrder = {"id", "name", "startDate", "endDate"})
public class ConflictSoapDTO {
    @XmlElement(required = true)
    private int id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String endDate;
    public ConflictSoapDTO(Conflict conflict) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        this.id = conflict.getId();
        this.name = conflict.getName();
        if(conflict.getStartDate() != null){
            this.startDate = conflict.getStartDate().format(formatter);
        } else{
            this.startDate = null;
        }
        if(conflict.getEndDate() != null){
            this.endDate = conflict.getEndDate().format(formatter);
        } else{
            this.endDate = null;
        }
    }
}

