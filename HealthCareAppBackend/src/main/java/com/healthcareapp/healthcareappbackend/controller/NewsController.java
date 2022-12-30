package com.healthcareapp.healthcareappbackend.controller;

import com.healthcareapp.healthcareappbackend.dto.NewsDto;
import com.healthcareapp.healthcareappbackend.service.NewsService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @PostMapping(path = "/saveNews")
    public NewsDto saveNews(NewsDto newsDto) {
        return newsService.save(newsDto);
    }

    @GetMapping(path = "/getNewsFromApi")
    public List<NewsDto> getNewsFromApi() throws IOException, JSONException, ParseException {
        return newsService.getNewsFromApi();
    }
}
