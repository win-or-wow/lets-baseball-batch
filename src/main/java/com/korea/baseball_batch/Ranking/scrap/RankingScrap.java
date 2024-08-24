package com.korea.baseball_batch.Ranking.scrap;

import com.korea.baseball_batch.Ranking.service.RankingService;
import com.korea.baseball_batch.config.ScrapingConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class RankingScrap {

    @Value("${scrap.ranking}")
    private String rankingUrl;

    private final ScrapingConfig scrapingConfig;
    private final RankingService rankingService;

    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void getInfo() {
        Document document = scrapingConfig.requestJsoup(rankingUrl);

        // th에서 '순위'를 찾은 후 해당 컬럼 index를 return: first
        Element table = document.select("#cphContents_cphContents_cphContents_udpRecord table").get(0);
        Elements th = table.select("tr > th");
        Optional<Element> ranking = th.stream().filter(e -> StringUtils.equals(e.text(), "순위")).findFirst();
        Optional<Element> team = th.stream().filter(e -> StringUtils.equals(e.text(), "팀명")).findFirst();

        if(ranking.isEmpty() || team.isEmpty()) {
            throw new IllegalStateException("팀 순위를 조회할 수 없습니다.");
        }

        int rankingIndex = ranking.get().elementSiblingIndex() + 1;
        int teamIndex = team.get().elementSiblingIndex() + 1;

        String rankingSelect = "td:nth-of-type(" + rankingIndex + ")";
        String teamSelect = "td:nth-of-type(" + teamIndex + ")";

        Elements tr = table.select("tr");
        tr.stream().skip(1).forEach(e -> {
            String nowRanking = e.select(rankingSelect).text();
            String teamName = e.select(teamSelect).text();

            rankingService.update(teamName, nowRanking);
        });
    }
}
