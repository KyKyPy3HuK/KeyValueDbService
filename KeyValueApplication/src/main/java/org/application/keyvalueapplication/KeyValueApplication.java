package org.application.keyvalueapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeyValueApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeyValueApplication.class, args);
    }

}
