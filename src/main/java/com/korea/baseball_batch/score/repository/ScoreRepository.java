package com.korea.baseball_batch.score.repository;

import com.korea.baseball_batch.common.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByGameDateAndHomeTeamId(LocalDate gameDate, Long homeTeamdId);
}
