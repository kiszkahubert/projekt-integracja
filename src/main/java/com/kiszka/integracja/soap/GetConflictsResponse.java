package com.kiszka.integracja.soap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "getConflictsResponse", namespace = "http://kiszka.com/integracja")
public class GetConflictsResponse {
    private List<ConflictSoapDTO> conflict;
    @XmlElement(name = "conflict")
    public List<ConflictSoapDTO> getConflict() {
        if (conflict == null) conflict = new java.util.ArrayList<>();
        return conflict;
    }
}
