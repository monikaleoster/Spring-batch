package io.spring.chunk;

import io.spring.controller.RestApplication;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class ChunksConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean("chunkReader")
    public ItemReader<Line> itemReader(){
        return new LineReader();
    }

    @Bean("chunkProcessor")
    public ItemProcessor<Line, Line> itemProcessor(){
        return new LineItemProcessor();
    }

    @Bean("chunkWriter")
    public ItemWriter<Line> itemWriter() {
        return new LinesWriter();
    }

    @Bean(name ="chunkstep")
    protected Step step1(@Qualifier("chunkReader")ItemReader<Line> reader,
                         @Qualifier("chunkProcessor")ItemProcessor<Line, Line> processor,@Qualifier("chunkWriter") ItemWriter<Line> writer) {
        return stepBuilderFactory.get("processLines").<Line, Line> chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name= "chunksJob")
    public Job job() {
        return jobBuilderFactory
                  .get("chunksJob").incrementer(new RunIdIncrementer())
                .start(step1(itemReader(), itemProcessor(), itemWriter()))
                .build();
    }




}
