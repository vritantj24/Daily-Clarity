package com.example.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private String url;

    @Column(name = "img_url")
    private String imageUrl;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    private String content;

    private String source;

    private String author;

    private String category;
}