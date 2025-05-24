package com.kiszka.integracja.soap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "getConflictsResponse", namespace = "http://kiszka.com/integracja")
public class GetConflictsResponse {

    private List<ConflictSoapDTO> conflict;

    @XmlElement(name = "conflict")
    public List<ConflictSoapDTO> getConflict() {
        if (conflict == null) conflict = new java.util.ArrayList<>();
        return conflict;
    }

    public void setConflict(List<ConflictSoapDTO> conflict) {
        this.conflict = conflict;
    }
}
