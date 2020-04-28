package io.spring.chunk;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChunkRestApplication {


    public static void main(String[] args) {
        new SpringApplication(ChunkRestApplication.class).run(args);
    }
}
