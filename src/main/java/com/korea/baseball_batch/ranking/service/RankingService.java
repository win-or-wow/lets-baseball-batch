package com.korea.baseball_batch.ranking.service;

import com.korea.baseball_batch.common.entity.Team;
import com.korea.baseball_batch.ranking.repository.RankingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public void update(Team team) {
        Optional<Team> hasTeam = rankingRepository.findByTeamId(team.getTeamId());
        Team getTeam = hasTeam.orElseThrow(() -> new IllegalStateException("해당 팀은 존재하지 않습니다."));

        getTeam.rankingUpdate(team);
    }
}
