package org.application.keyvalueapplication.component;

import jakarta.annotation.PostConstruct;
import org.application.keyvalueapplication.repositories.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class Deleter {
    private final DataRepository dataRepository;

    @Autowired
    public Deleter(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Async
    @Scheduled(fixedDelay = 1)
    void deleteOldRow(){
        System.out.println("Deleting old data");
        dataRepository.deleteOldData();
    }
}
