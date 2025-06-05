package com.kiszka.integracja.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kiszka.integracja.DTOs.CommodityDTO;
import com.kiszka.integracja.DTOs.CommodityTypeDTO;
import com.kiszka.integracja.entities.CommodityType;
import com.kiszka.integracja.entities.Conflict;
import com.kiszka.integracja.services.DataService;
import com.kiszka.integracja.soap.ConflictSoapDTO;
import com.kiszka.integracja.soap.GetConflictsResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/data")
public class DataController {
    private final DataService dataService;
    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }
    @GetMapping("/commodities/types/all")
    public List<CommodityTypeDTO> getAllCommoditiesType(){
        return dataService.getAllCommodityTypes();
    }
        @GetMapping("/commodities")
    public List<CommodityDTO> getCommoditiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return dataService.getCommoditiesByDateRange(startDate, endDate);
    }
    @GetMapping("/commodities/{commodityTypeId}")
    public List<CommodityDTO> getCommoditiesByTypeAndDateRange(
            @PathVariable int commodityTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return dataService.getCommoditiesByTypeAndDateRange(commodityTypeId, startDate, endDate);
    }
    @PostMapping("/commodities/import-csv")
    public void importCommodities(@RequestParam("file")MultipartFile file){
        dataService.importCommoditiesFromCSV(file);
    }
    @PostMapping("/commodities/import-json")
    public void importCommoditiesFromJson(@RequestParam("file") MultipartFile file) {
        dataService.importCommoditiesFromJSON(file);
    }
    @GetMapping("/commodities/export")
    public void exportCommodities(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response
    ) throws IOException {
        final var commodities = dataService.getCommoditiesByDateRange(startDate, endDate);
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=commodities.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = mapper.writeValueAsString(commodities);
        response.getWriter().write(jsonString);
        response.getWriter().flush();
    }
    @GetMapping(value = "/conflicts/export-xml", produces = MediaType.APPLICATION_XML_VALUE)
    public void exportConflictsToXml(HttpServletResponse response) throws IOException, JAXBException {
        List<Conflict> conflicts = dataService.getAllConflicts();
        GetConflictsResponse responseObj = new GetConflictsResponse();
        conflicts.forEach(c -> responseObj.getConflict().add(new ConflictSoapDTO(c)));
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=conflicts.xml");
        JAXBContext context = JAXBContext.newInstance(GetConflictsResponse.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //bo samodzielnie łącze fragmenty xmla
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        response.getWriter().write("""
        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
            <SOAP-ENV:Header/>
            <SOAP-ENV:Body>
        """);
        marshaller.marshal(responseObj, response.getWriter());
        response.getWriter().write("""
            </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
        """);
        response.getWriter().flush();
    }
    @PostMapping(value = "/conflicts/import-xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importConflictsFromXml(@RequestParam("file") MultipartFile file) {
        try {
            String xmlContent = new String(file.getBytes());
            dataService.importConflictsFromXml(xmlContent);
        } catch (Exception e) {}
    }
}