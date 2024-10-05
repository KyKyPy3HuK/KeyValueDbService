package com.punk_pozer.KeyValueDbService.component;

import com.punk_pozer.KeyValueDbService.repository.KeyValueWithTtlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class DatabaseGarbageCollector {

    private final KeyValueWithTtlRepository keyValueWithTtlRepository;


    @Autowired
    public DatabaseGarbageCollector(KeyValueWithTtlRepository keyValueWithTtlRepository){
        this.keyValueWithTtlRepository = keyValueWithTtlRepository;
    }


    @Async
    @Scheduled(fixedRateString = "${DatabaseGarbageCollector.fixedRateDelayMillis}")
    public void collectGarbage(){
        if(keyValueWithTtlRepository.deleteAllExpired() > 0){
            System.out.println("Garbage collected");
        };
    }
}
