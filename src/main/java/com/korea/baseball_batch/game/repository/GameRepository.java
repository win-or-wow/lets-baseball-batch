package com.korea.baseball_batch.game.repository;

import com.korea.baseball_batch.common.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
