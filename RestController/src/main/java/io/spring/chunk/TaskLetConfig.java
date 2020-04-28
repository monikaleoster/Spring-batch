package io.spring.chunk;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class TaskLetConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;


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

            return RepeatStatus.FINISHED;
        });

    }
}
