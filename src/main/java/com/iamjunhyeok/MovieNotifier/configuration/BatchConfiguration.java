package com.iamjunhyeok.MovieNotifier.configuration;

import com.iamjunhyeok.MovieNotifier.batch.ChainedItemWriter;
import com.iamjunhyeok.MovieNotifier.batch.CustomJobExecutionListener;
import com.iamjunhyeok.MovieNotifier.batch.DataStorageItemWriter;
import com.iamjunhyeok.MovieNotifier.batch.FilterByUserItemProcessor;
import com.iamjunhyeok.MovieNotifier.batch.SendNotificationItemWriter;
import com.iamjunhyeok.MovieNotifier.batch.WebCrawlingItemReader;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.service.NotificationService;
import com.iamjunhyeok.MovieNotifier.service.UserService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final UserService userService;

    private final NotificationService notificationService;

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step())
                .listener(new CustomJobExecutionListener(webCrawlingItemReader(), notificationService))
                .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("step", jobRepository)
                .<Movie, Map<Movie, List<User>>>chunk(10, platformTransactionManager)
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
    @StepScope
    public FilterByUserItemProcessor filterByUserItemProcessor() {
        return new FilterByUserItemProcessor(userService, notificationService);
    }

    @Bean
    public ChainedItemWriter chainedItemWriter(DataStorageItemWriter dataStorageItemWriter, SendNotificationItemWriter sendNotificationItemWriter) {
        return new ChainedItemWriter(dataStorageItemWriter, sendNotificationItemWriter);
    }

    @Bean
    public DataStorageItemWriter dataStorageItemWriter() {
        return new DataStorageItemWriter((JpaItemWriter<Movie>) jpaItemWriter());
    }

    @Bean
    public SendNotificationItemWriter sendNotificationItemWriter() {
        return new SendNotificationItemWriter((JpaItemWriter<Notification>) jpaItemWriter());
    }

    public JpaItemWriter<?> jpaItemWriter() {
        JpaItemWriter<?> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
