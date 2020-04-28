package io.spring.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

public class RestApplication {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean("Testjob")
    public Job job(){
        return this.jobBuilderFactory.get("Testjob").incrementer(new RunIdIncrementer()).start(step1()).build();
    }

    @Bean
    public Step step1(){
        return this.stepBuilderFactory.get("step1").tasklet((helloWorldTasklet())).build();
    }

    @Bean
    public Tasklet helloWorldTasklet() {

        return ((stepContribution, chunkContext) -> {
            for(int i =0;i<10;i++) {
                System.out.println("Step Ran"+i);
            }
            return RepeatStatus.FINISHED;
        });

    }

    public static void main(String[] args) {
        new SpringApplication(RestApplication.class).run(args);
    }
}
