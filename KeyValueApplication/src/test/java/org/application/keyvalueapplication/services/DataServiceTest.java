package org.application.keyvalueapplication.services;

import org.application.keyvalueapplication.component.Deleter;
import org.application.keyvalueapplication.model.Data;
import org.application.keyvalueapplication.repositories.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class DataServiceTest {

    @MockBean
    private DataRepository dataRepository;

    @MockBean
    private Deleter deleter;

    @Autowired
    private DataService dataService;


    @Test
    public void givenNewValue_whenKeyExists_thenUpdateValue() {
        String key = "testKey";
        String value = "newValue";
        int ttl = 3600;

        given(dataRepository.getDataByKey(key)).willReturn(value);

        dataService.addNewValue(key, value, ttl);

        verify(dataRepository).updateDataByKey(eq(key),eq(value),any(Date.class));
        verify(dataRepository, never()).save(any(Data.class));
    }

    @Test
    public void givenNewValue_whenKeyDoesNotExist_thenSaveNewData() {
        String key = "testKey";
        String value = "newValue";
        int ttl = 3600;

        given(dataRepository.getDataByKey(key)).willReturn(null);


        dataService.addNewValue(key, value, ttl);

        verify(dataRepository).save(any(Data.class));
        verify(dataRepository, never()).updateDataByKey(eq(key), eq(value), any(Date.class));
    }

    @Test
    public void givenKey_whenGetDataByKey_thenReturnValue() {
        String key = "testKey";
        String expectedValue = "testValue";

        given(dataRepository.getDataByKey(key)).willReturn(expectedValue);

        String actualValue = dataService.getValueByKey(key);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void giventRemoveValue_whenRemoveValue_thenReturnAndDeleteValue() {
        String key = "testKey";
        String expectedValue = "testValue";

        given(dataRepository.getDataByKey(key)).willReturn(expectedValue);

        String actualValue = dataService.removeValue(key);

        verify(dataRepository).deleteValueByKey(key);
        assertEquals(expectedValue, actualValue);
    }



}
