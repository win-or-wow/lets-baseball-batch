package com.korea.baseball_batch.Ranking.enums;

import lombok.Getter;

@Getter
public enum Teams {
    LG(1L),
    KT(2L),
    SSG(3L),
    NC(4L),
    두산(5L),
    KIA(6L),
    롯데(7L),
    삼성(8L),
    한화(9L),
    키움(10L);

    private Long teamId;

    Teams(Long teamId) {
        this.teamId = teamId;
    }
}