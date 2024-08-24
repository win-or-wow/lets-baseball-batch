package com.korea.baseball_batch.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
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
}
