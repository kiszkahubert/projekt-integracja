package com.kiszka.integracja.controllers;

import com.kiszka.integracja.entities.Conflict;
import com.kiszka.integracja.services.DataService;
import com.kiszka.integracja.soap.ConflictSoapDTO;
import com.kiszka.integracja.soap.GetConflictsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class ConflictEndpoint {

    private static final String NAMESPACE_URI = "http://kiszka.com/integracja";

    private final DataService dataService;

    @Autowired
    public ConflictEndpoint(DataService dataService) {
        this.dataService = dataService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getConflictsRequest")
    @ResponsePayload
    public GetConflictsResponse getConflicts() {
        List<Conflict> conflictList = dataService.getAllConflicts();
        GetConflictsResponse response = new GetConflictsResponse();
        conflictList.forEach(c -> response.getConflict().add(new ConflictSoapDTO(c)));
        return response;
    }
}
