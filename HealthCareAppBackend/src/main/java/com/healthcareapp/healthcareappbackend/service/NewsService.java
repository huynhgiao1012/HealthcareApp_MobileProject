package com.healthcareapp.healthcareappbackend.service;

import com.healthcareapp.healthcareappbackend.converter.NewsMapper;
import com.healthcareapp.healthcareappbackend.dto.NewsDto;
import com.healthcareapp.healthcareappbackend.entity.NewsEntity;
import com.healthcareapp.healthcareappbackend.repository.NewsRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsMapper newsMapper;

    public NewsDto save(NewsDto newsDto) {
        // Dont save if the title is existed
        if (newsRepository.findByTitle(newsDto.getTitle()).isPresent()) {
            return null;
        }

        // If title is new then save
        NewsEntity newsEntity = newsRepository.save(newsMapper.dtoToEntity(newsDto));
        return newsMapper.entityToDto(newsEntity);
    }

    public List<NewsDto> getNewsFromApi() throws IOException, JSONException, ParseException {
        List<NewsDto> newsDtoList = new ArrayList<>();
        // Get news from api

        // 1. Create a request
        String GET_URL = "https://newsapi.org/v2/everything?q=medical&apiKey=915e44de7fa74270961a55b477feee90";
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        // 2. reading response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        String content = "";
        while ((inputLine = in.readLine()) != null) {
            content += inputLine;
        }
        JSONObject json = new JSONObject(content);
        JSONArray newsList = json.getJSONArray("articles");

        Instant instant = null;
        for (int i=0; i<newsList.length(); i++) {
            JSONObject news = newsList.getJSONObject(i);

            NewsDto newsDto = new NewsDto();

            newsDto.setTitle(String.valueOf(news.get("title")));
            newsDto.setDescription(String.valueOf(news.get("description")));
            newsDto.setAuthorName(String.valueOf(news.get("author")));
            newsDto.setImage(String.valueOf(news.get("urlToImage")));
            newsDto.setPublicDate(Date.from(instant.parse(String.valueOf(news.get("publishedAt")))));

            newsDtoList.add(newsDto);
        }
        in.close();

        return newsDtoList;
    }
}
