package com.korea.baseball_batch.Ranking.repository;

import com.korea.baseball_batch.Ranking.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamId(Long teamId);
}
