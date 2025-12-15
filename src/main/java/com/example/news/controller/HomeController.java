package com.example.news.controller;

import com.example.news.response.LatestNewsResponse;
import com.example.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/")
    public String home(Model model) {

        LatestNewsResponse news = newsService.fetchLatest();

        model.addAttribute("featuredArticle", news.getFeaturedArticle());
        model.addAttribute("latestArticles", news.getLatestArticles());
        model.addAttribute("categories", news.getCategories());
        model.addAttribute("trendingArticles", news.getTrendingArticles());

        return "home";
    }
}
