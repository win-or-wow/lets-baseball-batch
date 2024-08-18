package com.korea.baseball_batch.Ranking.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long teamId;
    Long ballparkId;
    String name;
    @Nullable
    Integer rank;

    public void rankingUpdate(Team team) {
        this.teamId = team.getTeamId();
        this.rank = team.getRank();
    }
}
