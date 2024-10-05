package com.korea.baseball_batch.config;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.score.scrap.ScoreScrap;
import com.korea.baseball_batch.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ScoreBatchConfig {

    private final ScoreScrap scoreScrap;
    private final ScoreService scoreService;

    @Bean
    public Job syncScoreJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("syncScoreJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(getTodayScore(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step getTodayScore(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        FaultTolerantStepBuilder<Object, Object> stepBuilder = new StepBuilder("getTodayScore", jobRepository)
                .chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .reader(todayScoreReader())
                .processor(todayScoreProcessor())
                .writer(todayScoreWriter())
                .faultTolerant()
                .retry(IllegalStateException.class)
                .retryLimit(3);

        return stepBuilder
                .retryPolicy(retryPolicy())
                .build();
    }

    protected ItemReader<Game> todayScoreReader() {
        return new ListItemReader<>(scoreScrap.getTodayScore());
    }

    protected ItemProcessor<? super Object, ?> todayScoreProcessor() {
        return item -> item;
    }

    protected ItemWriter<? super Object> todayScoreWriter() {
        return items -> items.forEach((item) -> scoreService.update((Game) item));
    }

    private RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(IllegalStateException.class, true);
        return new SimpleRetryPolicy(3, retryableExceptions);
    }

}
