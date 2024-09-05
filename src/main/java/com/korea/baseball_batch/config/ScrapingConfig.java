package com.korea.baseball_batch.config;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration
public class ScrapingConfig {

    @Value("${path.web-driver}")
    private String WEB_DRIVER_PATH;

    // 동적페이지 스크래핑
    public WebDriver requestSelenium(String url) {
        System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new ChromeDriver(options);

        driver.get(url);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

        return driver;
    }

    // 정적페이지 스크래핑
    public Document requestJsoup(String url) {
        Connection connection = Jsoup.connect(url); // 스크래핑 요청 url
        Document document = null;

        try {
            document = connection.get();
        } catch(IOException e) {
            log.error("해당 페이지에 접속할 수 없습니다.");
        }
        return document;
    }
}
