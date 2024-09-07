package com.korea.baseball_batch.score.scrap;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.common.enums.Status;
import com.korea.baseball_batch.common.enums.Teams;
import com.korea.baseball_batch.config.ScrapingConfig;
import com.korea.baseball_batch.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScoreScrap {

    @Value("${scrap.score}")
    private String scoreUrl;

    private final ScrapingConfig scrapingConfig;
    private final ScoreService scoreService;

    public List<Game> getTodayScore() {
        WebDriver driver = scrapingConfig.requestSelenium(scoreUrl);
        List<Game> gameList = new ArrayList<>();

        try {
            WebElement ulTag = findFirstElement(driver, By.className("ScheduleAllType_match_list__3n5L_"));
            List<WebElement> liTags = ulTag.findElements(By.className("MatchBox_item_content__3SGZf"));

            for (WebElement li : liTags) {
                String gameStatus = extractText(li, By.className("MatchBox_status__2pbzi"));
                String homeTeam = extractTeamName(li, 1);

                if (isGameFinished(gameStatus)) {
                    int awayTeamScore = extractTeamScore(li, 0);
                    int homeTeamScore = extractTeamScore(li, 1);

                    Game getGame = findByHomeTeamIdAndGameDate(homeTeam);

                    Game game = Game.builder()
                            .gameId(getGame.getGameId())
                            .homeTeamScore(homeTeamScore)
                            .awayTeamScore(awayTeamScore)
                            .build();

                    gameList.add(game);
                }
            }
        } catch (Exception e) {
            log.error("스크래핑을 실행할 수 없습니다.", e);
        } finally {
            driver.quit();
        }

        return gameList;
    }

    private WebElement findFirstElement(WebDriver driver, By by) {
        return driver.findElements(by).get(0);
    }

    private String extractText(WebElement element, By by) {
        return element.findElement(by).getText();
    }

    private String extractTeamName(WebElement li, int index) {
        return li.findElements(By.className("MatchBoxTeamArea_team__3aB4O")).get(index).getText();
    }

    private int extractTeamScore(WebElement li, int index) {
        return Integer.parseInt(li.findElements(By.className("MatchBoxTeamArea_score__1_YFB")).get(index).getText());
    }

    private boolean isGameFinished(String gameStatus) {
        return Status.종료 == Status.valueOf(gameStatus);
    }

    private Game findByHomeTeamIdAndGameDate(String homeTeam) {
        return scoreService.findByHomeTeamIdAndGameDate(Teams.valueOf(homeTeam).getTeamId(), LocalDate.now().minusDays(1))
                .orElseThrow(() -> new IllegalStateException("게임이 존재하지 않습니다."));
    }
}