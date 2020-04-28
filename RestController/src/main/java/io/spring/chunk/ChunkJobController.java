package io.spring.chunk;

import io.spring.controller.JobLaunchRequest;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ChunkJobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobExplorer jobExplorer;

    @PostMapping(path= "/runJob1")
    public ExitStatus runJob(@RequestBody JobLaunchRequest request) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
            ApplicationContextProvider.setContext(context);
        Job job = this.context.getBean(request.getName(), Job.class);

        //JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(),this.jobExplorer).getNextJobParameters(job).toJobParameters();
        Map<String, JobParameter> jobparams = new HashMap<>();
        jobparams.put("timeStamp", new JobParameter(LocalDateTime.now().toString()));
        JobParameters jobParameters = new JobParameters(jobparams);
        return this.jobLauncher.run(job,jobParameters).getExitStatus();
    }
}
