package com.example.news.response;

import com.example.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestNewsResponse {
    private News featuredArticle;
    private List<News> latestArticles;
    private List<String> categories;
    private List<News> trendingArticles;
    private String breakingNews;
}
