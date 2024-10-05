package com.korea.baseball_batch.config;

import com.korea.baseball_batch.common.entity.Team;
import com.korea.baseball_batch.ranking.scrap.RankingScrap;
import com.korea.baseball_batch.ranking.service.RankingService;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = "syncRankingJob")
public class RankingBatchConfig {

    private final RankingScrap rankingScrap;
    private final RankingService rankingService;

    @Bean
    public Job syncRankingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("syncRankingJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(getRanking(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step getRanking(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        FaultTolerantStepBuilder<Object, Object> stepBuilder = new StepBuilder("getRanking", jobRepository)
                .chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .reader(rankingReader())
                .processor(rankingProcessor())
                .writer(rankingWriter())
                .faultTolerant()
                .retry(IllegalStateException.class)
                .retryLimit(3);

        return stepBuilder
                .retryPolicy(retryPolicy())
                .build();
    }

    protected ItemReader<Team> rankingReader() {
        return new ListItemReader<>(rankingScrap.getRanking());
    }

    protected ItemProcessor<? super Object, ?> rankingProcessor() {
        return item -> item;
    }

    protected ItemWriter<? super Object> rankingWriter() {
        return items -> items.forEach((item) -> rankingService.update((Team) item));
    }

    private RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(IllegalStateException.class, true);
        return new SimpleRetryPolicy(3, retryableExceptions);
    }

}
