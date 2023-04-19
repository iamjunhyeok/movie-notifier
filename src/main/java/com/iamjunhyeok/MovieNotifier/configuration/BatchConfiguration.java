package com.iamjunhyeok.MovieNotifier.configuration;

import com.iamjunhyeok.MovieNotifier.batch.ChainedItemWriter;
import com.iamjunhyeok.MovieNotifier.batch.CustomJobExecutionListener;
import com.iamjunhyeok.MovieNotifier.batch.DataStorageItemWriter;
import com.iamjunhyeok.MovieNotifier.batch.FilterByUserItemProcessor;
import com.iamjunhyeok.MovieNotifier.batch.SendNotificationItemWriter;
import com.iamjunhyeok.MovieNotifier.batch.WebCrawlingItemReader;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step())
                .listener(new CustomJobExecutionListener())
                .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("step", jobRepository)
                .<Movie, Movie>chunk(10, platformTransactionManager)
                .reader(webCrawlingItemReader())
                .processor(filterByUserItemProcessor())
                .writer(chainedItemWriter(dataStorageItemWriter(), sendNotificationItemWriter()))
                .build();
    }

    @Bean
    public WebCrawlingItemReader webCrawlingItemReader() {
        return new WebCrawlingItemReader();
    }

    @Bean
    public FilterByUserItemProcessor filterByUserItemProcessor() {
        return new FilterByUserItemProcessor();
    }

    @Bean
    public ChainedItemWriter chainedItemWriter(DataStorageItemWriter dataStorageItemWriter, SendNotificationItemWriter sendNotificationItemWriter) {
        return new ChainedItemWriter(dataStorageItemWriter, sendNotificationItemWriter);
    }

    @Bean
    public DataStorageItemWriter dataStorageItemWriter() {
        return new DataStorageItemWriter();
    }

    @Bean
    public SendNotificationItemWriter sendNotificationItemWriter() {
        return new SendNotificationItemWriter();
    }
}
