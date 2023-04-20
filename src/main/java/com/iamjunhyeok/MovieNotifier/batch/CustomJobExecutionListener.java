package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
@RequiredArgsConstructor
public class CustomJobExecutionListener implements JobExecutionListener {

    private final WebCrawlingItemReader webCrawlingItemReader;

    private final NotificationService notificationService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        webCrawlingItemReader.init();
        log.info("{} is beginning execution", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        notificationService.sendNotification();
        log.info("{} has completed with the status {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}
