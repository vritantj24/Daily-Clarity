package com.example.news.service;

import com.example.news.response.LatestNewsResponse;
import org.springframework.stereotype.Service;

@Service
public interface NewsService {
    LatestNewsResponse fetchLatest();
    String generateNews();
}