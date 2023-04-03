package com.agasen.springk8s.domain;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarksDTO {
  private List<BookmarkDTO> data;
  private long totalElements;
  private int totalPages;
  private int currentPage;

  @JsonProperty("isFirst")
  private boolean isFirst;

  @JsonProperty("isLast")
  private boolean isLast;

  private boolean hasNext;
  private boolean hasPrevious;

  public BookmarksDTO(Page<BookmarkDTO> bookmarkPage) {
    this.data = bookmarkPage.getContent();
    this.totalElements = bookmarkPage.getTotalElements();
    this.totalPages = bookmarkPage.getTotalPages();
    this.currentPage = bookmarkPage.getNumber() + 1;
    this.isFirst = bookmarkPage.isFirst();
    this.isLast = bookmarkPage.isLast();
    this.hasNext = bookmarkPage.hasNext();
    this.hasPrevious = bookmarkPage.hasPrevious();
  }
}
