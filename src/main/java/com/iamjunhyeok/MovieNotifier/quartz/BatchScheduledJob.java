package com.iamjunhyeok.MovieNotifier.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalTime;

@RequiredArgsConstructor
public class BatchScheduledJob extends QuartzJobBean {

    private final Job job;

    private final JobExplorer jobExplorer;

    private final JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(this.job)
                .addLocalTime("timestamp", LocalTime.now())
                .toJobParameters();

        try {
            this.jobLauncher.run(this.job, jobParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
