package com.korea.baseball_batch.score.service;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.score.repository.ScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public Optional<Game> findByHomeTeamIdAndGameDate(Long gameId, LocalDate today) {
        return scoreRepository.findByHomeTeamIdAndGameDate(gameId, today);
    }

    public Optional<Game> findByGameIdAndGameDate(Long gameId, LocalDate today) {
        return scoreRepository.findByGameIdAndGameDate(gameId, today);
    }

    @Transactional
    public void update(Game game) {
        LocalDate updateDate = LocalDate.now().minusDays(1);

        System.out.println(game.getGameId() + " : " + updateDate);

        Optional<Game> hasGame = findByGameIdAndGameDate(game.getGameId(), updateDate);
        Game getGame = hasGame.orElseThrow(() -> new IllegalStateException("해당 게임은 존재하지 않습니다."));

        Game updateScore = Game.builder()
                .gameId(getGame.getGameId())
                .homeTeamScore(game.getHomeTeamScore())
                .awayTeamScore(game.getAwayTeamScore())
                .build();

        getGame.gameScoreUpdate(updateScore);
    }
}
