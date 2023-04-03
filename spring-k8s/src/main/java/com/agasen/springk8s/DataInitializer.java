package com.agasen.springk8s;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.agasen.springk8s.domain.Bookmark;
import com.agasen.springk8s.domain.BookmarkRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
  private final BookmarkRepository bookmarkRepository;

  @Override
  public void run(String... args) throws Exception {
    bookmarkRepository.save(new Bookmark(null, "SivaLabs", "https://sivalabs.in", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "SpringBlog", "https://spring.io/blog", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "Quarkus", "https://quarkus.io/", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "Micronaut", "https://micronaut.io/", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "JOOQ", "https://www.jooq.org/", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "VladMihalcea", "https://vladmihalcea.com", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "Thoughts On Java", "https://thorben-janssen.com", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "DZone", "https://dzone.com", Instant.now()));
    bookmarkRepository.save(new Bookmark(null, "DevOpsBookmarks", "https://www.devopsbookmarks.com/", Instant.now()));
  }

}
