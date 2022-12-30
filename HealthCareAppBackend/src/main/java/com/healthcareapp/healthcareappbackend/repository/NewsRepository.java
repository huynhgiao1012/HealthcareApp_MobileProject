package com.healthcareapp.healthcareappbackend.repository;

import com.healthcareapp.healthcareappbackend.entity.NewsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface NewsRepository extends CrudRepository<NewsEntity, Long> {
    @Override
    NewsEntity save(NewsEntity news);

    @Query(value = "select news from NewsEntity news where news.title=?1")
    Optional<NewsEntity> findByTitle(String title);
}
