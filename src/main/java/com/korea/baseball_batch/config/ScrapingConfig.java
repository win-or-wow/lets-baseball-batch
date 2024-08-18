package com.korea.baseball_batch.config;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class ScrapingConfig {

    public Document request(String url) {
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
