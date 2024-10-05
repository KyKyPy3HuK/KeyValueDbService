package org.application.keyvalueapplication.services;

import org.application.keyvalueapplication.model.Data;
import org.application.keyvalueapplication.repositories.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public void addNewValue(String key, String value, int ttl) {
        try {
            if (dataRepository.getDataByKey(key) != null) {
                dataRepository.updateDataByKey(key, value, Date.from(Instant.now().plusSeconds(ttl)));
            } else {
                Data data = new Data();
                data.setKey(key);
                data.setValue(value);
                data.setTtl(Date.from(Instant.now().plusSeconds(ttl)));
                dataRepository.save(data);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String getValueByKey(String key) {
        return dataRepository.getDataByKey(key);
    }

    public String removeValue(String key){
        String res = dataRepository.getDataByKey(key);
        dataRepository.deleteValueByKey(key);
        return res;
    }
}
