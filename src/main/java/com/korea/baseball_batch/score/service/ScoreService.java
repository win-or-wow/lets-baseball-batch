package com.korea.baseball_batch.score.service;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.score.repository.ScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    @Value("${today}")
    private LocalDate today;

    private final ScoreRepository scoreRepository;

    public Optional<Game> findByHomeTeamIdAndGameDate(Long homeTeamId, LocalDate today) {
        return scoreRepository.findByHomeTeamIdAndGameDate(homeTeamId, today);
    }

    public Optional<Game> findByGameIdAndGameDate(Long gameId, LocalDate today) {
        return scoreRepository.findByGameIdAndGameDate(gameId, today);
    }

    @Transactional
    public void update(Game game) {
        Optional<Game> hasGame = findByGameIdAndGameDate(game.getGameId(), today);
        Game getGame = hasGame.orElseThrow(() -> new IllegalStateException("해당 게임은 존재하지 않습니다."));

        Game updateScore = Game.builder()
                .gameId(getGame.getGameId())
                .homeTeamScore(game.getHomeTeamScore())
                .awayTeamScore(game.getAwayTeamScore())
                .build();

        getGame.gameScoreUpdate(updateScore);
    }
}
