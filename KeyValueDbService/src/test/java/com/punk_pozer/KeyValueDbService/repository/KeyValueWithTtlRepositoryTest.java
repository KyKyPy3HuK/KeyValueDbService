package com.punk_pozer.KeyValueDbService.repository;


import com.punk_pozer.KeyValueDbService.model.KeyValueWithTtl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Date;

@DataJpaTest
public class KeyValueWithTtlRepositoryTest {

    @Autowired
    KeyValueWithTtlRepository repository;

    @Test
    void getValueByKey_keyExist_returnValue(){
        String key = "someKey";
        String value = "someValue";

        repository.save(new KeyValueWithTtl(key, value, new Date()));

        assert value.equals(repository.getValueByKey(key));
    }

    @Test
    void getValueByKey_keyNotExist_returnNull(){
        String key = "someKey";

        assert repository.getValueByKey(key) == null;
    }

    @Test
    void save_keyNotExist_addNewRecord(){
        String key = "notExistingKey";
        String value = "someValue";

        repository.save(new KeyValueWithTtl(key,value,new Date()));
        assert repository.findByKey(key).getKey().equals(key);
    }

    @Test
    void updateByKey_keyExist_updateRecord(){
        String key = "someKey";
        String value = "someValue";
        Date date = new Date(0L);

        repository.save(new KeyValueWithTtl(key,value,date));

        String newValue = "newValue";
        Date newDate = new Date(1000L);

        repository.updateByKey(key,newValue,newDate);

        assert repository.getValueByKey(key).equals(newValue);
    }

    @Test
    void removeByKey_keyExist_removeRecord(){
        String key = "someKey";
        String value = "someValue";
        Date date = new Date(0L);

        repository.save(new KeyValueWithTtl(key,value,date));

        repository.removeByKey(key);

        assert repository.getValueByKey(key) == null;
    }

    @Test
    void deleteAllExpired_notExpiredRecord_nothing(){

        KeyValueWithTtl notExpiredRecord = new KeyValueWithTtl("notExpiredRecord", "someData",
                Date.from(Instant.now().plusSeconds(1)));

        repository.save(notExpiredRecord);

        repository.deleteAllExpired();

        assert repository.getValueByKey("notExpiredRecord") == "someData";
    }

    @Test
    void deleteAllExpired_expiredRecord_delete(){

        KeyValueWithTtl expiredRecord = new KeyValueWithTtl("expiredRecord", "someData",
                new Date(0));

        repository.save(expiredRecord);

        repository.deleteAllExpired();

        assert repository.getValueByKey("expiredRecord") == null;
    }

    @Test
    void deleteAllExpired_infinityTtlRecord_nothing(){

        KeyValueWithTtl infinityTtlRecord = new KeyValueWithTtl("infinityTtlRecord", "someData",
                null);

        repository.save(infinityTtlRecord);

        repository.deleteAllExpired();

        assert repository.getValueByKey("infinityTtlRecord") == "someData";
    }

    @Test
    void deleteAllExpired_differentRecords_deleteOnlyExpired(){

        KeyValueWithTtl notExpiredRecord = new KeyValueWithTtl("notExpiredRecord", "someData",
                Date.from(Instant.now().plusSeconds(1)));
        KeyValueWithTtl infinityTtlRecord = new KeyValueWithTtl("infinityTtlRecord", "someData",
                null);
        KeyValueWithTtl expiredRecord = new KeyValueWithTtl("expiredRecord", "someData",
                new Date(0));

        repository.save(notExpiredRecord);
        repository.save(infinityTtlRecord);
        repository.save(expiredRecord);

        repository.deleteAllExpired();

        assert repository.getValueByKey("notExpiredRecord") == "someData"
                && repository.getValueByKey("infinityTtlRecord") == "someData"
                && repository.getValueByKey("expiredRecord") == null;
    }
}
