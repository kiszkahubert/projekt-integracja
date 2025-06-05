package com.kiszka.integracja.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kiszka.integracja.DTOs.CommodityDTO;
import com.kiszka.integracja.DTOs.CommodityTypeDTO;
import com.kiszka.integracja.entities.CommodityType;
import com.kiszka.integracja.entities.Conflict;
import com.kiszka.integracja.entities.Commodity;
import com.kiszka.integracja.repositories.CommodityTypeRepository;
import com.kiszka.integracja.repositories.ConflictsRepository;
import com.kiszka.integracja.repositories.CommodityRepository;
import com.kiszka.integracja.soap.ConflictSoapDTO;
import com.kiszka.integracja.soap.GetConflictsResponse;
import com.opencsv.CSVReader;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataService {
    private final ConflictsRepository conflictsRepository;
    private final CommodityRepository commodityRepository;
    private final CommodityTypeRepository commodityTypeRepository;
    public DataService(
            ConflictsRepository conflictsRepository,
            CommodityRepository commodityRepository,
            CommodityTypeRepository commodityTypeRepository
    ) {
        this.conflictsRepository = conflictsRepository;
        this.commodityRepository = commodityRepository;
        this.commodityTypeRepository = commodityTypeRepository;
    }
    public List<CommodityTypeDTO> getAllCommodityTypes(){
        return commodityTypeRepository.findAll()
                .stream()
                .map(this::convertTypeToDto)
                .collect(Collectors.toList());

    }
    public List<Conflict> getAllConflicts() {
        return conflictsRepository.findAll();
    }
    public List<CommodityDTO> getCommoditiesByDateRange(LocalDate startDate, LocalDate endDate) {
        return commodityRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private CommodityDTO convertToDto(Commodity commodity) {
        CommodityType type = commodity.getCommodityType();
        CommodityTypeDTO typeDto = new CommodityTypeDTO(
                type.getId(),
                type.getName(),
                type.getCategory(),
                type.getQuote()
        );
        return new CommodityDTO(
                commodity.getCommodityId(),
                commodity.getDate(),
                commodity.getPrice(),
                typeDto
        );
    }
    private CommodityTypeDTO convertTypeToDto(CommodityType type){
        return new CommodityTypeDTO(
                type.getId(),
                type.getName(),
                type.getCategory(),
                type.getQuote()
        );
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<CommodityDTO> getCommoditiesByTypeAndDateRange(int commodityTypeId, LocalDate startDate, LocalDate endDate) {
        return commodityRepository.findByCommodityTypeAndDateBetween(commodityTypeId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    /***
     * 0=id 1=date 2=price 3=type_id 4=name 5=category 6=quote
     */
    public void importCommoditiesFromCSV(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                int id = Integer.parseInt(row[0]);
                LocalDate date = LocalDate.parse(row[1]);
                double price = Double.parseDouble(row[2]);
                int typeId = Integer.parseInt(row[3]);
                String typeName = row[4];
                String category = row[5];
                String quote = row[6];
                CommodityType type = commodityTypeRepository.findById(typeId)
                        .orElseGet(() -> {
                            CommodityType newType = new CommodityType();
                            newType.setId(typeId);
                            newType.setName(typeName);
                            newType.setCategory(category);
                            newType.setQuote(quote);
                            return commodityTypeRepository.save(newType);
                        });
                Commodity commodity = new Commodity();
                commodity.setCommodityId(id);
                commodity.setCommodityType(type);
                commodity.setDate(date);
                commodity.setPrice(price);
                commodityRepository.save(commodity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void importCommoditiesFromJSON(MultipartFile file) {
        try {
            log.info("stating json import");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            CommodityDTO[] commodityDTOs = mapper.readValue(file.getInputStream(), CommodityDTO[].class);
            for (CommodityDTO commodityDTO : commodityDTOs) {
                CommodityType type = commodityTypeRepository.findById(commodityDTO.getType().getId())
                        .orElseGet(() -> {
                            CommodityType newType = new CommodityType();
                            newType.setName(commodityDTO.getType().getName());
                            newType.setCategory(commodityDTO.getType().getCategory());
                            newType.setQuote(commodityDTO.getType().getQuote());
                            return commodityTypeRepository.save(newType);
                        });
                Commodity commodity = new Commodity();
                commodity.setCommodityType(type);
                commodity.setDate(commodityDTO.getDate());
                commodity.setPrice(commodityDTO.getPrice());
                commodityRepository.save(commodity);
            }
            log.info("import finished");
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void importConflictsFromXml(String xmlContent){
        try{
            String bodyContent = extractSoapBody(xmlContent);
            JAXBContext context = JAXBContext.newInstance(GetConflictsResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            GetConflictsResponse response = (GetConflictsResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(bodyContent)));
            for (var obj : response.getConflict()) {
                Conflict conflict = new Conflict();
                conflict.setName(obj.getName());
                conflict.setStartDate(LocalDate.parse(obj.getStartDate()));
                if (obj.getEndDate() != null && !obj.getEndDate().isEmpty()) {
                    conflict.setEndDate(LocalDate.parse(obj.getEndDate()));
                }
                conflictsRepository.save(conflict);
            }
        } catch (Exception e){
            log.info(e.getMessage());
        }

    }

    private String extractSoapBody(String xmlContent) {
        int bodyStart = xmlContent.indexOf("<SOAP-ENV:Body>") + "<SOAP-ENV:Body>".length();
        int bodyEnd = xmlContent.indexOf("</SOAP-ENV:Body>");
        return xmlContent.substring(bodyStart, bodyEnd).trim();
    }
}