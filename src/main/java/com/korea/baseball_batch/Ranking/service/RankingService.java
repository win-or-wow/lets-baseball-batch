package com.korea.baseball_batch.Ranking.service;

import com.korea.baseball_batch.Ranking.entity.Team;
import com.korea.baseball_batch.Ranking.enums.Teams;
import com.korea.baseball_batch.Ranking.repository.RankingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public void update(String teamName, String nowRanking) {
        Team updateTeam = Team.builder()
                .teamId(Teams.valueOf(teamName).getTeamId())
                .rank(Integer.parseInt(nowRanking))
                .build();

        Optional<Team> hasTeam = rankingRepository.findByTeamId(updateTeam.getTeamId());
        Team getTeam = hasTeam.orElseThrow(() -> new IllegalStateException("해당 팀은 존재하지 않습니다."));

        getTeam.rankingUpdate(updateTeam);
    }
}
