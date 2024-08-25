package com.korea.baseball_batch.score.service;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.common.enums.Status;
import com.korea.baseball_batch.common.enums.Teams;
import com.korea.baseball_batch.score.repository.ScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.korea.baseball_batch.util.StringUtil.dateFormat;
import static com.korea.baseball_batch.util.StringUtil.timeFormat;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public void save(String homeTeam, String awayTeam, String date, String time) {
        Game game = Game.builder()
                .homeTeamId(Teams.valueOf(homeTeam).getTeamId())
                .awayTeamId(Teams.valueOf(awayTeam).getTeamId())
                .gameDate(dateFormat(date))
                .gameTime(timeFormat(time))
                .status(Status.예정.toString())
                .build();

        scoreRepository.save(game);
    }

    @Transactional
    public void update(String homeTeam, int homeTeamScore, int awayTeamScore) {
        // TODO testDate 변경
        LocalDate testDate = LocalDate.now().minusDays(1);
//        LocalDate.now();

        Optional<Game> hasGame = scoreRepository.findByGameDateAndHomeTeamId(testDate, Teams.valueOf(homeTeam).getTeamId());
        Game getGame = hasGame.orElseThrow(() -> new IllegalStateException("해당 게임은 존재하지 않습니다."));

        Game updateGame = Game.builder()
                .gameId(getGame.getGameId())
                .homeTeamScore(homeTeamScore)
                .awayTeamScore(awayTeamScore)
                .build();

        getGame.gameScoreUpdate(updateGame);
    }
}
