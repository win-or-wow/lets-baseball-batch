package com.korea.baseball_batch.score.repository;

import com.korea.baseball_batch.common.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByHomeTeamIdAndGameDate(Long homeTeamId, LocalDate gameDate);
    Optional<Game> findByGameIdAndGameDate(Long gameId, LocalDate gameDate);
}
