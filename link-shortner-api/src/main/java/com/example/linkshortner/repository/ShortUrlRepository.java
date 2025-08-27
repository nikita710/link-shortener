package com.example.linkshortner.repository;

import com.example.linkshortner.model.ShortUrl;
import com.example.linkshortner.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortenedCode(String shortenedCode);

    boolean existsShortUrlByShortenedCode(String shortenedCode);

    Page<ShortUrl> findAllByUser(User owner, Pageable pageable);

    Optional<ShortUrl> findByUserAndShortenedCode(User user, String shortenedCode);
}
