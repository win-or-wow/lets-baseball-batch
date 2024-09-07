package com.korea.baseball_batch.game.scrap;

import com.korea.baseball_batch.common.entity.Game;
import com.korea.baseball_batch.common.enums.Teams;
import com.korea.baseball_batch.config.ScrapingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static com.korea.baseball_batch.util.SplitUtil.splitDate;
import static com.korea.baseball_batch.util.StringUtil.dateFormat;
import static com.korea.baseball_batch.util.StringUtil.timeFormat;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameScrap {

    @Value("${scrap.game}")
    private String gameUrl;

    private final ScrapingConfig scrapingConfig;

    public List<Game> getGame() {
        WebDriver driver = scrapingConfig.requestSelenium(gameUrl);
        List<Game> gameList = new ArrayList<>();

        try {
            WebElement element = findFirstElement(driver, By.className("ScheduleLeagueType_match_list_container__1v4b0"));
            List<WebElement> trTags = element.findElements(By.className("ScheduleLeagueType_match_list_group__18ML9"));

            for (WebElement tr : trTags) {
                String date = extractDate(tr.findElement(By.className("ScheduleLeagueType_title__2Kalm")).getText());
                List<WebElement> games = tr.findElements(By.className("MatchBox_item_content__3SGZf"));

                games.forEach(row -> gameList.add(createGame(row, date)));
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

    private String extractDate(String beforeFormattingDate) {
        return splitDate(beforeFormattingDate, "\\(", 0);
    }

    private Game createGame(WebElement row, String date) {
        String time = extractText(row, By.className("MatchBox_time__nIEfd")).split("\n")[1].trim();
        String stadium = extractText(row, By.className("MatchBox_stadium__13gft")).split("\n")[1].trim();
        List<WebElement> teams = row.findElements(By.className("MatchBoxTeamArea_name_info__2IaZV"));

        String awayTeam = teams.get(0).getText();
        String homeTeam = teams.get(1).getText().split("\n")[0].trim();

        return Game.builder()
                .homeTeamId(getTeamId(homeTeam))
                .awayTeamId(getTeamId(awayTeam))
                .gameDate(dateFormat(date))
                .gameTime(timeFormat(time))
                .build();
    }

    private String extractText(WebElement element, By by) {
        return element.findElement(by).getText();
    }

    private long getTeamId(String teamName) {
        return Teams.valueOf(teamName).getTeamId();
    }
}
