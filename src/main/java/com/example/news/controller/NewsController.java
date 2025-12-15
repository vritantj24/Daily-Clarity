package com.example.news.controller;

import com.example.news.response.LatestNewsResponse;
import com.example.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("fetch-latest")
    public ResponseEntity<LatestNewsResponse> getLatestNews() {
        return ResponseEntity.ok().body(newsService.fetchLatest());
    }

    @Scheduled(fixedRate = 1000 * 60 * 120, initialDelay = 1000 * 60 * 60)
    @PostMapping("admin/generate")
    public ResponseEntity<String> generateNews () {
        String response = newsService.generateNews();
        if(response != null && response.contains("Successfully")) {
            return ResponseEntity.ok().body(response);
        }else {
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
