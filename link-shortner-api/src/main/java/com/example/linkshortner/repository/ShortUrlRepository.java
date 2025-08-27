package com.example.linkshortner.repository;

import com.example.linkshortner.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortenedCode(String shortenedCode);

    boolean existsShortUrlByShortenedCode(String shortenedCode);
}
