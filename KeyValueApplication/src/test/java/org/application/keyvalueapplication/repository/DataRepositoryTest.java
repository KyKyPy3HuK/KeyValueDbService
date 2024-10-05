package org.application.keyvalueapplication.repository;

import jakarta.transaction.Transactional;
import org.application.keyvalueapplication.model.Data;
import org.application.keyvalueapplication.repositories.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DataRepositoryTest {

    @Autowired
    private DataRepository dataRepository;


    @Test
    public void givenKet_whenGetDataByKey_thenReturnValue() {
        Data data = new Data();
        data.setKey("testKey");
        data.setValue("testValue");
        data.setTtl(Date.from(Instant.now().plusSeconds(100)));
        dataRepository.save(data);

        String value = dataRepository.getDataByKey("testKey");

        assertEquals("testValue", value);
    }

    @Test
    @Transactional
    public void givenOldData_whenUpdateData_thenValueUpdate() {
        Data data = new Data();
        data.setKey("testKey");
        data.setValue("oldValue");
        data.setTtl(Date.from(Instant.now().plusSeconds(100)));
        dataRepository.save(data);

        Date newTtl = Date.from(Instant.now().plusSeconds(100));
        dataRepository.updateDataByKey("testKey", "newValue",newTtl);

        String actualValue = dataRepository.getDataByKey("testKey");
        assertEquals("newValue", actualValue);
    }

    @Test
    @Transactional
    public void testDeleteValueByKey() {
        Data data = new Data();
        data.setKey("testKey");
        data.setValue("testValue");
        data.setTtl(Date.from(Instant.now().plusSeconds(100)));
        dataRepository.save(data);

        dataRepository.deleteValueByKey("testKey");

        assertNull(dataRepository.getDataByKey("testKey"));
    }

    @Test
    @Transactional
    public void testDeleteOldData() {
        Data oldData = new Data();
        oldData.setKey("oldKey");
        oldData.setValue("oldValue");
        oldData.setTtl(Date.from(Instant.now().minusSeconds(100)));
        dataRepository.save(oldData);

        dataRepository.deleteOldData();

        assertNull(dataRepository.getDataByKey("oldKey"));
    }


}
