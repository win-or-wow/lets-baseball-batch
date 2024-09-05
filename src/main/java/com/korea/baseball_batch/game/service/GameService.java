package com.korea.baseball_batch.game.service;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public void save(Game game) {
        gameRepository.save(game);
    }
}
