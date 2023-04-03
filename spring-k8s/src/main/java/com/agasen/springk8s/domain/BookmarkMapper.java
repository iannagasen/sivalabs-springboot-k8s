package com.agasen.springk8s.domain;

import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper implements Function<Bookmark, BookmarkDTO> {

  @Override
  public BookmarkDTO apply(Bookmark b) {
    var dto = new BookmarkDTO();
    dto.setId(b.getId());
    dto.setTitle(b.getTitle());
    dto.setUrl(b.getUrl());
    dto.setCreatedAt(b.getCreatedAt());
    return dto;
  }
}
