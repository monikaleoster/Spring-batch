package io.spring.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

public class JobLaunchingController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobExplorer jobExplorer;

    public ExitStatus runJob( @RequestBody JobLaunchRequest request) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    Job job = this.context.getBean(request.getName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(),this.jobExplorer).getNextJobParameters(job).toJobParameters();
    return this.jobLauncher.run(job,jobParameters).getExitStatus();
    }

}
