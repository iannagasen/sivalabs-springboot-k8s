package com.agasen.springk8s.domain;

import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper implements Function<Bookmark, BookmarkDTO> {

  @Override
  public BookmarkDTO apply(Bookmark b) {
    return new BookmarkDTO(b.getId(), b.getTitle(), b.getUrl(), b.getCreatedAt());
  }
}
