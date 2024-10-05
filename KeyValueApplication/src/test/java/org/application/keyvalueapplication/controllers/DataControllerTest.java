package org.application.keyvalueapplication.controllers;

import org.application.keyvalueapplication.services.DataService;
import org.application.keyvalueapplication.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataService dataService;

    @MockBean
    private FileService fileService;

    @Test
    public void givenNewData_whenAddData_thenStatusAndContentSuccess() throws Exception {
        doNothing().when(dataService).addNewValue(anyString(), anyString(), anyInt());

        mockMvc.perform(post("/add")
                        .param("key", "testKey")
                        .param("value", "testValue")
                        .param("ttl", "100"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Успешно добавлено"));
    }

    @Test
    public void givenNewData_whenServiceThrowException_thenReturnFailedStatus() throws Exception {
        doThrow(new RuntimeException("Test Exception")).when(dataService).addNewValue(anyString(), anyString(), anyInt());

        mockMvc.perform(post("/add")
                        .param("key", "testKey")
                        .param("value", "testValue"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Ошибка при добавлении или обновлении данных: Test Exception"));
    }

    @Test
    public void givenKey_whenGetValueByKey_thenReturnSuccessValue() throws Exception {
        when(dataService.getValueByKey("testKey")).thenReturn("testValue");

        mockMvc.perform(get("/get")
                        .param("key", "testKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("testValue"));
    }

    @Test
    public void givenNotExistKet_whenGetValueByKey_thenReturnStatusNotFound() throws Exception {
        when(dataService.getValueByKey("testKey")).thenReturn(null);

        mockMvc.perform(get("/get")
                        .param("key", "testKey"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Значение не найдено"));
    }

    @Test
    public void givenExistKey_whenRemoveValueByKey_thenReturnDeleteString() throws Exception {
        when(dataService.removeValue("testKey")).thenReturn("testValue");

        mockMvc.perform(delete("/remove")
                        .param("key", "testKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("Удаленная строка: testValue"));
    }

    @Test
    public void givenNotExistValue_whenRemoveValue_thenReturnFailed() throws Exception {
        when(dataService.removeValue("testKey")).thenReturn(null);

        mockMvc.perform(delete("/remove")
                        .param("key", "testKey"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Значение не найдено"));
    }

    @Test
    public void givenDump_whenGetAllData_thenReturnSuccess() throws Exception {
        doNothing().when(fileService).getAllData();

        mockMvc.perform(get("/dump"))
                .andExpect(status().isOk())
                .andExpect(content().string("Данные успешно выгружены в файл"));
    }

    @Test
    public void givenData_whenLoadAllData_thenReturnSuccessLoad() throws Exception {
        doNothing().when(fileService).loadAllData();

        mockMvc.perform(get("/load"))
                .andExpect(status().isOk())
                .andExpect(content().string("Данные успешно загружены из файла"));
    }

}
