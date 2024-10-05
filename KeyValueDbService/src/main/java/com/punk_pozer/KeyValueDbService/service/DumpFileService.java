package com.punk_pozer.KeyValueDbService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punk_pozer.KeyValueDbService.model.DumpFile;
import com.punk_pozer.KeyValueDbService.model.DumpMode;
import com.punk_pozer.KeyValueDbService.model.KeyValueWithTtl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class DumpFileService {

    @Value("${DumpFile.fileName}")
    private Path fileName;

    @PostConstruct
    private void init(){
        try {
            if(!Files.exists(fileName)){
                Files.createFile(fileName);
            }
        }
        catch (IOException e){
            System.out.println("IO ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public DumpFileService(){
    }

    public void saveDump(List<KeyValueWithTtl> data, DumpMode mode) throws IOException{
        // Изменение данных в зависимости от режима
        switch(mode){
            case SOFT:{
                // Вычисление остатка TTL
                Date timestamp = Date.from(Instant.now());
                for(KeyValueWithTtl elem : data){
                    if (elem.getTtl()!= null){
                        elem.setTtl(new Date(elem.getTtl().getTime() - timestamp.getTime()));
                    }
                }
                break;
            }
            case DEFAULT:{break;}
            default:{break;}

        }

        DumpFile dumpFile = new DumpFile(mode, data);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dumpFile);
        Files.writeString(fileName, json);
    }

    public List<KeyValueWithTtl> loadDump() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = Files.readString(fileName);
        DumpFile dumpFile = mapper.readValue(json, DumpFile.class);
        List<KeyValueWithTtl> data = dumpFile.getKeyValueWithTtlList();

        // Операции с данными в зависимости от режима
        switch(dumpFile.getMode()){
            case SOFT:{
                // Восстановление TTL по остатку и текущему времени
                Date timestamp = Date.from(Instant.now());
                for (var elem : data){
                    if (elem.getTtl()!= null){
                        elem.setTtl(new Date(elem.getTtl().getTime() + timestamp.getTime()));
                    }
                }
                break;
            }
            case DEFAULT:{
                break;
            }
            default:{
                break;
            }
        }

        return data;
    }

}
