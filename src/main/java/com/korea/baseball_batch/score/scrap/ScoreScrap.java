package com.korea.baseball_batch.score.scrap;

import com.korea.baseball_batch.common.enums.Status;
import com.korea.baseball_batch.config.ScrapingConfig;
import com.korea.baseball_batch.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScoreScrap {

    @Value("${scrap.score}")
    private String scoreUrl;

    private final ScrapingConfig scrapingConfig;
    private final ScoreService scoreService;

    @Scheduled(fixedDelay = 5000) // 5초마다 실행
    public void getInfo() {
        WebDriver driver = scrapingConfig.requestSelenium(scoreUrl);

        try {
            WebElement ulTag = driver.findElements(By.className("ScheduleAllType_match_list__3n5L_")).get(0);
            List<WebElement> liTags = ulTag.findElements(By.className("MatchBox_item_content__3SGZf"));
            for (WebElement li : liTags) {
                String gameStatus = li.findElement(By.className("MatchBox_status__2pbzi")).getText();
                String homeTeam = li.findElements(By.className("MatchBoxTeamArea_team__3aB4O")).get(1).getText();

                if(Status.종료 == Status.valueOf(gameStatus)) {
                    int awayTeamScore = Integer.parseInt(li.findElements(By.className("MatchBoxTeamArea_score__1_YFB")).get(0).getText());
                    int homeTeamScore = Integer.parseInt(li.findElements(By.className("MatchBoxTeamArea_score__1_YFB")).get(1).getText());

                    scoreService.update(homeTeam, homeTeamScore, awayTeamScore);
                }

                // TODO
                if(Status.취소 == Status.valueOf(gameStatus)) {
                    // 경기일정 status update
                }


            }
        } catch(Exception e) {
            log.error("스크래핑을 실행할 수 없습니다.");
        } finally {
//            driver.close();
            driver.quit();
        }

    }
}