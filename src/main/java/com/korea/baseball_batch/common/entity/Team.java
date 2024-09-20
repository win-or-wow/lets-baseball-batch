package com.korea.baseball_batch.common.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(schema="baseball")
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
