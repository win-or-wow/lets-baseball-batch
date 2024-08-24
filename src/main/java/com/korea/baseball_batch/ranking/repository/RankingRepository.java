package com.korea.baseball_batch.ranking.repository;

import com.korea.baseball_batch.common.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamId(Long teamId);
}
