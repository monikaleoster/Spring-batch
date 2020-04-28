package io.spring.chunk.campaignjob;

import io.spring.chunk.Line;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import javax.sql.DataSource;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class CampaignJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean("campaignReader")
    public ItemReader<Line> campaignReader(){

        return CampaignJDBCReader.createCampaignJDBCProvider(dataSource);
    }

    @Bean("campaignProcessor")
    public ItemProcessor<Line, Line> campaignProcessor(){
        return new CampaignProcessor();
    }

    @Bean("campaignWriter")
    public ItemWriter<Line> campaignStatusUpdater() {
        return new CampaignWriter();
    }

    @Bean(name ="campaignCreateStep")
    protected Step step(@Qualifier("campaignReader")ItemReader<Line> reader,
                        @Qualifier("campaignProcessor")ItemProcessor<Line, Line> processor,@Qualifier("campaignWriter") ItemWriter<Line> writer) {
        return stepBuilderFactory.get("processLines").<Line, Line> chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


    @Bean(name= "campaignJob")
    public Job job() {
        return jobBuilderFactory
                .get("campaignJob").incrementer(new RunIdIncrementer())
                .start(step(campaignReader(), campaignProcessor(), campaignStatusUpdater()))
                .build();
    }


    private AtomicBoolean enabled = new AtomicBoolean(true);

    private AtomicInteger batchRunCounter = new AtomicInteger(0);

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    @Scheduled(fixedRate = 20000)
    public void launchJob() throws Exception {
        Date date = new Date();
        System.out.println("scheduler starts at " + date);
        if (enabled.get()) {
            JobExecution jobExecution = jobLauncher().run(job(), new JobParametersBuilder().addDate("launchDate", date)
                    .toJobParameters());
            batchRunCounter.incrementAndGet();
            System.out.println("Batch job ends with status as " + jobExecution.getStatus());
        }
        System.out.println("scheduler ends ");
    }

    public void stop() {
        enabled.set(false);
    }

    public void start() {
        enabled.set(true);
    }

    @Bean
    public TaskScheduler poolScheduler() {
        return new CustomTaskScheduler();
    }

    private class CustomTaskScheduler extends ThreadPoolTaskScheduler {

        private static final long serialVersionUID = -7142624085505040603L;

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }

    }

    public void cancelFutureSchedulerTasks() {
        scheduledTasks.forEach((k, v) -> {
            if (k instanceof CampaignJobConfig) {
                v.cancel(false);
            }
        });
    }


    @Bean(name = "schedulerJobLauncher")
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean(name ="schedulerJobRepository")
    public JobRepository jobRepository() throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        factory.setTransactionManager(new ResourcelessTransactionManager());
        return ( JobRepository) factory.getObject();
    }




    public AtomicInteger getBatchRunCounter() {
        return batchRunCounter;
    }


}
