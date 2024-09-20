package com.korea.baseball_batch.ranking.scrap;

import com.korea.baseball_batch.common.entity.Team;
import com.korea.baseball_batch.common.enums.Teams;
import com.korea.baseball_batch.config.ScrapingConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RankingScrap {

    @Value("${scrap.ranking}")
    private String rankingUrl;

    private final ScrapingConfig scrapingConfig;

    public List<Team> getRanking() {
        Document document = scrapingConfig.requestJsoup(rankingUrl);

        // th에서 '순위'를 찾은 후 해당 컬럼 index를 return: first
        Element table = document.select("#cphContents_cphContents_cphContents_udpRecord table").get(0);

        int rankingIndex = getColumnIndex(table, "순위");
        int teamIndex = getColumnIndex(table, "팀명");

        if (rankingIndex == -1 || teamIndex == -1) {
            throw new IllegalStateException("팀 순위를 조회할 수 없습니다.");
        }

        String rankingSelect = "td:nth-of-type(" + (rankingIndex + 1) + ")";
        String teamSelect = "td:nth-of-type(" + (teamIndex + 1) + ")";

        List<Team> teamList = new ArrayList<>();
        Elements rows = table.select("tr");
        rows.stream().skip(1).map(row -> createTeam(row, rankingSelect, teamSelect)).forEach(teamList::add);

        return teamList;
    }

    private int getColumnIndex(Element table, String columnName) {
        Elements th = table.select("tr > th");
        return th.stream()
                .filter(e -> StringUtils.equals(e.text(), columnName))
                .findFirst()
                .map(Element::elementSiblingIndex)
                .orElse(-1);
    }

    private Team createTeam(Element row, String rankingSelect, String teamSelect) {
        String nowRanking = row.select(rankingSelect).text();
        String teamName = row.select(teamSelect).text();

        return Team.builder()
                .teamId(getTeamIdByName(teamName))
                .rank(parseRank(nowRanking))
                .build();
    }

    private int parseRank(String ranking) {
        return Integer.parseInt(ranking);
    }

    private long getTeamIdByName(String teamName) {
        return Teams.valueOf(teamName).getTeamId();
    }
}
