package com.korea.baseball_batch.game.scrap;

import com.korea.baseball_batch.config.ScrapingConfig;
import com.korea.baseball_batch.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

import static com.korea.baseball_batch.util.SplitUtil.splitDate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameScrap {

    @Value("${scrap.play}")
    private String playUrl;

    private final ScrapingConfig scrapingConfig;
    private final GameService gameService;

    @Scheduled(fixedDelay = 20000) // 20초마다 실행
    public void getInfo() {
        WebDriver driver = scrapingConfig.requestSelenium(playUrl);

        try {
            WebElement element = driver.findElements(By.className("ScheduleLeagueType_match_list_container__1v4b0")).get(0);
            List<WebElement> trTags = element.findElements(By.className("ScheduleLeagueType_match_list_group__18ML9"));

            for (WebElement tr : trTags) {
                String beforeFormatingDate = tr.findElement(By.className("ScheduleLeagueType_title__2Kalm")).getText();
                String date = splitDate(beforeFormatingDate, "\\(", 0);

                List<WebElement> game = tr.findElements(By.className("MatchBox_item_content__3SGZf"));
                game.forEach(row -> {
                    String time = row.findElement(By.className("MatchBox_time__nIEfd")).getText().split("\n")[1].trim();
                    String stadium = row.findElement(By.className("MatchBox_stadium__13gft")).getText().split("\n")[1].trim();
                    List<WebElement> teams = row.findElements(By.className("MatchBoxTeamArea_name_info__2IaZV"));
                    String awayTeam = teams.get(0).getText();
                    String homeTeam = teams.get(1).getText().split("\n")[0].trim();

                    gameService.save(homeTeam, awayTeam, date, time);
                });
            }
        } catch(Exception e) {
            log.error("스크래핑을 실행할 수 없습니다.");
        } finally {
//            driver.close();
            driver.quit();
        }

    }
}
