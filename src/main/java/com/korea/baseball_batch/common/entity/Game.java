package com.korea.baseball_batch.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long gameId;
    Long homeTeamId;
    Long awayTeamId;
    LocalDate gameDate;
    LocalTime gameTime;
    int homeTeamScore;
    int awayTeamScore;
    String status;

    public void gameScoreUpdate(Game game) {
        this.gameId = game.gameId;
        this.homeTeamScore = game.homeTeamScore;
        this.awayTeamScore = game.awayTeamScore;
    }
}
