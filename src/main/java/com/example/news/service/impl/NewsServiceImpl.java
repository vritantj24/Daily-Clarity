package com.example.news.service.impl;

import com.example.news.entity.News;
import com.example.news.repository.NewsRepository;
import com.example.news.response.LatestNewsResponse;
import com.example.news.service.NewsService;
import com.example.news.service.NewsSseService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private RestTemplate restTemplate;
    private final List<String> categories = List.of("business","entertainment","general","health","science","sports","technology");
    @Value("${external.news-api.key}")
    private String apiKey;
    @Value("${external.news-api.url}")
    private String apiUrl;
    @Autowired
    private NewsSseService newsSseService;

    @Override
    public LatestNewsResponse fetchLatest() {
        List<News> newsList =  newsRepository.findTop20ByOrderByPublishedAtDesc();
        if(newsList==null || newsList.isEmpty()){
            return new LatestNewsResponse();
        }
        return new LatestNewsResponse(
                newsList.getFirst(),
                newsList,
                categories,
                newsList.subList(0,Math.min(newsList.size(),5))
        );
    }

    @Override
    public String generateNews() {
        Random random = new Random();
        int randomIndex = random.nextInt(categories.size());
        String category = categories.get(randomIndex);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            String url = apiUrl + "/top-headlines" + "?apiKey=" + apiKey + "&category=" + category + "&pageSize=5";
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class);

            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode data = response.getBody();
                if(data==null){
                    return "Unable to generate News";
                }
                if(!data.get("status").asText().equalsIgnoreCase("ok")){
                    return "Unable to generate News";
                }

                JsonNode articles = data.get("articles");
                if(articles==null || articles.isEmpty()){
                    return "Unable to generate News";
                }
                List<News> newsList = new ArrayList<>();
                for(JsonNode article : articles){
                    News news = new News();
                    news.setSource(article.get("source").get("name").asText());
                    news.setAuthor(article.get("author").asText());
                    news.setTitle(article.get("title").asText());
                    news.setDescription(article.get("description").asText());
                    news.setUrl(article.get("url").asText());
                    news.setImageUrl(article.get("urlToImage").asText());
                    news.setPublishedAt(LocalDateTime.ofInstant(Instant.parse(article.get("publishedAt").asText()), ZoneId.systemDefault()));
                    news.setContent(article.get("content").asText());
                    news.setCategory(category);

                    newsList.add(news);
                }

                newsRepository.saveAll(newsList);
                newsSseService.sendNewsUpdateEvent();

                return "News Generated Successfully";
            }else{
                return "Unable to generate News";
            }
        } catch (Exception e) {
            log.error("Error making API request",e);
            return "Unable to generate News";
        }
    }
}
