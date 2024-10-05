package org.application.keyvalueapplication.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.application.keyvalueapplication.model.Data;
import org.application.keyvalueapplication.repositories.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private FileService fileService;

    private Path file;

    @BeforeEach
    public void setUp() {
        fileService = new FileService(dataRepository);
        file = Path.of("test_file.json");
        ReflectionTestUtils.setField(fileService, "file", file);
    }

    @Test
    public void givenData_whenGetAllData_thenWriteFile() throws IOException {
        List<Data> mockData = List.of(new Data(), new Data());
        when(dataRepository.findAll()).thenReturn(mockData);
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedData = objectMapper.writeValueAsString(mockData);

        fileService.getAllData();

        assertTrue(Files.exists(file));
        String writtenData = Files.readString(file);
        assertEquals(expectedData, writtenData);
    }


    @Test
    public void givenData_whenLoadAllData_thenDataSave() throws IOException {
        List<Data> mockData = List.of(new Data(), new Data());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(mockData);
        Files.writeString(file, jsonData);

        fileService.loadAllData();

        verify(dataRepository).deleteAll();
        verify(dataRepository).saveAll(any(List.class));
    }
}
