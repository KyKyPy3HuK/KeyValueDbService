package com.punk_pozer.KeyValueDbService.controller;

import com.punk_pozer.KeyValueDbService.model.DumpMode;
import com.punk_pozer.KeyValueDbService.service.KeyValueWithTtlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class KeyValueWithTtlApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeyValueWithTtlService keyValueWithTtlService;

    @Test
    void testConnection_success_returnOkStatus() throws Exception{
        mockMvc.perform(get("/api")).andExpect(status().isOk());
    }

    @Test
    void get_keyExist_returnValue() throws Exception{
        when(keyValueWithTtlService.getValue("someKey")).thenReturn("someValue");

        mockMvc.perform(get("/api/get")
                        .param("key", "someKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("someValue"));
    }


    @Test
    void get_keyNotExist_returnNotFoundStatus() throws Exception{
        when(keyValueWithTtlService.getValue("someKey")).thenReturn(null);

        mockMvc.perform(get("/api/get")
                        .param("key", "someKey"))
                .andExpect(status().isNotFound());
    }


    @Test
    void set_keyNotExist_addNewWithTtl() throws Exception{
        when(keyValueWithTtlService.set("someKey", "someValue",120L))
                .thenReturn(HttpStatus.CREATED);

        mockMvc.perform(post("/api/set")
                .param("key", "someKey")
                .param("value", "someValue")
                .param("ttl", "120"))
                .andExpect(status().isCreated());
    }


    @Test
    void set_keyNotExist_addNewWithoutTtl() throws Exception{
        when(keyValueWithTtlService.set("someKey", "someValue",-1L))
                .thenReturn(HttpStatus.CREATED);

        mockMvc.perform(post("/api/set")
                        .param("key", "someKey")
                        .param("value", "someValue"))
                .andExpect(status().isCreated());
    }


    @Test
    void set_keyExist_updateWithTtl() throws Exception{
        when(keyValueWithTtlService.set("someKey", "someValue",120L))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(post("/api/set")
                        .param("key", "someKey")
                        .param("value", "someValue")
                        .param("ttl", "120"))
                .andExpect(status().isOk());
    }


    @Test
    void set_keyExist_updateWithoutTtl() throws Exception{
        when(keyValueWithTtlService.set("someKey", "someValue",-1L))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(post("/api/set")
                        .param("key", "someKey")
                        .param("value", "someValue"))
                .andExpect(status().isOk());
    }


    @Test
    void remove_keyExist_returnValueAndRemove() throws Exception{
        when(keyValueWithTtlService.remove("someKey")).thenReturn(HttpStatus.OK);
        when(keyValueWithTtlService.getValue("someKey")).thenReturn("someValue");

        mockMvc.perform(post("/api/remove")
                        .param("key", "someKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("someValue"));
    }


    @Test
    void removeRequest_keyNotExist_returnNotFoundStatus() throws Exception{
        when(keyValueWithTtlService.remove("someKey")).thenReturn(HttpStatus.NOT_FOUND);
        when(keyValueWithTtlService.getValue("someKey")).thenReturn(null);

        mockMvc.perform(post("/api/remove")
                .param("key", "someKey"))
                .andExpect(status().isNotFound());
    }


    @Test
    void saveDump_defaultMode_returnOkStatus() throws Exception {
        when(keyValueWithTtlService.saveDump(DumpMode.DEFAULT)).thenReturn(HttpStatus.OK);

        mockMvc.perform(post("/api/dump/save"))
                .andExpect(status().isOk());
    }

    @Test
    void saveDump_softMode_returnOkStatus() throws Exception {
        when(keyValueWithTtlService.saveDump(DumpMode.SOFT)).thenReturn(HttpStatus.OK);

        mockMvc.perform(post("/api/dump/save")
                        .param("mode", "SOFT"))
                .andExpect(status().isOk());
    }

    @Test
    void saveDump_modeNotExist_returnBadRequestStatus() throws Exception {
        when(keyValueWithTtlService.saveDump(null)).thenReturn(HttpStatus.NOT_FOUND);

        mockMvc.perform(post("/api/dump/save")
                        .param("mode", "NOT_EXIST"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void saveDump_catchError_returnBadRequestStatus() throws Exception {
        when(keyValueWithTtlService.saveDump(DumpMode.DEFAULT)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        mockMvc.perform(post("/api/dump/save")
                        .param("mode", "DEFAULT"))
                .andExpect(status().is5xxServerError());
    }


    @Test
    void loadDump_success_returnOkStatus() throws Exception {
        when(keyValueWithTtlService.loadDump()).thenReturn(HttpStatus.OK);

        mockMvc.perform(post("/api/dump/load"))
                .andExpect(status().isOk());
    }


    @Test
    void loadDump_catchError_returnInternalServerErrorStatus() throws Exception{
        when(keyValueWithTtlService.loadDump()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        mockMvc.perform(post("/api/dump/load"))
                .andExpect(status().isInternalServerError());
    }
}
