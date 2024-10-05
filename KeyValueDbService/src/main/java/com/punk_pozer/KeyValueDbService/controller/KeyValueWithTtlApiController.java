package com.punk_pozer.KeyValueDbService.controller;

import com.punk_pozer.KeyValueDbService.model.DumpMode;
import com.punk_pozer.KeyValueDbService.model.KeyValueWithTtl;
import com.punk_pozer.KeyValueDbService.service.KeyValueWithTtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/api")
public class KeyValueWithTtlApiController {

    private final KeyValueWithTtlService keyValueWithTtlService;


    @Autowired
    public KeyValueWithTtlApiController(KeyValueWithTtlService keyValueWithTtlService){
        this.keyValueWithTtlService = keyValueWithTtlService;
    }


    @GetMapping("")
    public ResponseEntity<String> testConnection(){
        String message = "Connection successful!";
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }


    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestParam String key){
        String value = keyValueWithTtlService.getValue(key);
        if (value != null){
            return new ResponseEntity<String>(value, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("Key not found", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/set")
    public ResponseEntity<String> set(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam(required = false, defaultValue = "-1") Long ttl
    ){
        HttpStatus result = keyValueWithTtlService.set(key, value, ttl);
        String message;

        switch (result){
            case OK:{
                message = "Update successfully";
                break;
            }
            case CREATED:{
                message = "Create successfully";
                break;
            }
            default:{
                message = "Error occurred";
                break;
            }
        }

        return new ResponseEntity<String>(message, result);
    }


    @PostMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam String key){
        String value = keyValueWithTtlService.getValue(key);
        if (value != null){
            return new ResponseEntity<String>(value,
                    keyValueWithTtlService.remove(key));
        }
        else{
            return new ResponseEntity<String>("Key not found", HttpStatus.NOT_FOUND);
        }

    }


    @PostMapping("/dump/save")
    public ResponseEntity<String> saveDump(
            @RequestParam(name = "mode", required = false, defaultValue = "DEFAULT") DumpMode mode){
        HttpStatus result = keyValueWithTtlService.saveDump(mode);
        String message;
        if (result == HttpStatus.OK) {
            message = "Dump saved successfully";
        } else {
            message = "Error occurred";
        }
        return new ResponseEntity<String>(message,result);
    }


    @PostMapping("/dump/load")
    public ResponseEntity<String> loadDump(){
        HttpStatus result = keyValueWithTtlService.loadDump();
        String message;
        if (result == HttpStatus.OK) {
            message = "Dump loaded successfully";
        } else {
            message = "Error occurred";
        }
        return new ResponseEntity<String>(message,result);
    }
}
