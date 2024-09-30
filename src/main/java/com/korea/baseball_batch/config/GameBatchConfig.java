package com.korea.baseball_batch.config;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.game.scrap.GameScrap;
import com.korea.baseball_batch.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = "syncGameJob")
public class GameBatchConfig {

    private final GameScrap gameScrap;
    private final GameService gameService;

    @Bean
    public Job syncGameJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("syncGameJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(getGame(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step getGame(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("getGame", jobRepository)
            .chunk(50, transactionManager)
            .allowStartIfComplete(true) // TODO test 필요
            .reader(gameReader())
            .processor(gameProcessor())
            .writer(gameWriter())
            .build();
    }

    protected ItemReader<Game> gameReader() {
        return new ListItemReader<>(gameScrap.getGame());
    }

    protected ItemProcessor<? super Object, ?> gameProcessor() {
        return item -> item;
    }

    protected ItemWriter<? super Object> gameWriter() {
        return items -> items.forEach((item) -> gameService.save((Game) item));
    }

}
