package com.musat.musat_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MusatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusatBackendApplication.class, args);
    }

}
