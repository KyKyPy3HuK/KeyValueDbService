package org.application.keyvalueapplication.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.application.keyvalueapplication.model.Data;
import org.application.keyvalueapplication.repositories.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    @Value("${dump.file}")
    private Path file;

    private final DataRepository dataRepository;

    @Autowired
    public FileService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public void getAllData(){
        try {
            if (!Files.exists(file)){
                Files.createFile(file);
            }
            List<Data> all = dataRepository.findAll();
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(all);
            Files.writeString(file, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllData(){
        try {
            String data = Files.readAllLines(file).get(0);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Data> list = List.of(objectMapper.readValue(data, Data[].class));
            dataRepository.deleteAll();
            dataRepository.saveAll(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
