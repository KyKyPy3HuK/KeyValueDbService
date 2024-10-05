package org.application.keyvalueapplication.controllers;

import org.application.keyvalueapplication.services.DataService;
import org.application.keyvalueapplication.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DataController {
    private final DataService dataService;
    private final FileService fileService;

    @Autowired
    public DataController(DataService dataService, FileService fileService) {
        this.dataService = dataService;
        this.fileService = fileService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRow(@RequestParam("key") String key, @RequestParam("value") String value,
                                         @RequestParam(name = "ttl", required = false, defaultValue = "100") int ttl) {
        try {
            dataService.addNewValue(key, value, ttl);
            return new ResponseEntity<String>("Успешно добавлено", HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при добавлении или обновлении данных: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<String> getValue(@RequestParam("key") String key) {
        String valueByKey = dataService.getValueByKey(key);
        if (valueByKey != null) return new ResponseEntity<String>(valueByKey, HttpStatus.OK);
        else return new ResponseEntity<String>("Значение не найдено", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam("key") String key){
        String value = dataService.removeValue(key);
        if (value != null) return new ResponseEntity<String>("Удаленная строка: " + value, HttpStatus.OK);
        else return new ResponseEntity<String>("Значение не найдено", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dump")
    public ResponseEntity<String> dumpFile(){
        fileService.getAllData();
        return new ResponseEntity<String>("Данные успешно выгружены в файл",HttpStatus.OK);
    }

    @GetMapping("/load")
    public ResponseEntity<String> loadFile(){
        fileService.loadAllData();
        return new ResponseEntity<String>("Данные успешно загружены из файла",HttpStatus.OK);
    }
}
