package com.punk_pozer.KeyValueDbService.service;

import com.punk_pozer.KeyValueDbService.model.DumpMode;
import com.punk_pozer.KeyValueDbService.model.KeyValueWithTtl;
import com.punk_pozer.KeyValueDbService.repository.KeyValueWithTtlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class KeyValueWithTtlService {

    private final KeyValueWithTtlRepository keyValueWithTtlRepository;

    private final DumpFileService dumpFileService;

    @Autowired
    public KeyValueWithTtlService(
            KeyValueWithTtlRepository keyValueWithTtlRepository,
            DumpFileService dumpFileService
    ){
        this.keyValueWithTtlRepository = keyValueWithTtlRepository;
        this.dumpFileService = dumpFileService;
    }

    public String getValue(String key){
        return keyValueWithTtlRepository.getValueByKey(key);
    }

    public HttpStatus remove(String key){
        if (keyValueWithTtlRepository.removeByKey(key) > 0){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_FOUND;
        }
    }

    public HttpStatus set(String key, String value, Long ttl){
        Date date;
        if(ttl == -1){
            date = null;
        }
        else{
            date = Date.from(Instant.now().plusSeconds(ttl));
        }
        if(keyValueWithTtlRepository.findByKey(key) == null){
            keyValueWithTtlRepository.save(new KeyValueWithTtl(key, value, date));
            return HttpStatus.CREATED;
        }
        else{
            keyValueWithTtlRepository.updateByKey(key, value,date);
            return HttpStatus.OK;
        }
    }

    public HttpStatus saveDump(DumpMode mode){

        try {
            dumpFileService.saveDump(keyValueWithTtlRepository.findAll(), mode);
            return HttpStatus.OK;
        }
        catch (IOException e){
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public HttpStatus loadDump(){
        try {
            List<KeyValueWithTtl> keyValueWithTtl = dumpFileService.loadDump();
            keyValueWithTtlRepository.deleteAll();
            keyValueWithTtlRepository.saveAll(keyValueWithTtl);
            return HttpStatus.OK;
        }
        catch (IOException e){
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
