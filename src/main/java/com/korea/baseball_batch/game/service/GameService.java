package com.korea.baseball_batch.game.service;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.common.enums.Teams;
import com.korea.baseball_batch.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.korea.baseball_batch.util.StringUtil.dateFormat;
import static com.korea.baseball_batch.util.StringUtil.timeFormat;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public void save(String homeTeam, String awayTeam, String date, String time) {
        Game game = Game.builder()
                .homeTeamId(Teams.valueOf(homeTeam).getTeamId())
                .awayTeamId(Teams.valueOf(awayTeam).getTeamId())
                .gameDate(dateFormat(date))
                .gameTime(timeFormat(time))
                .build();

        gameRepository.save(game);
    }
}
