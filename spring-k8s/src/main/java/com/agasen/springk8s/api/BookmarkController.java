package com.agasen.springk8s.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agasen.springk8s.domain.BookmarksDTO;
import com.agasen.springk8s.domain.CreateBookmarkRequest;

import jakarta.validation.Valid;

import com.agasen.springk8s.domain.BookmarkDTO;
import com.agasen.springk8s.domain.BookmarkService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
  private final BookmarkService bookmarkService;

  /**
   * From user POV: page starts at 1
   * From Spring Jpa POV: page start at 0
   */
  @GetMapping
  public BookmarksDTO getBookmarks(
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "query", defaultValue = "") String query) {

    if (query == null || query.trim().isEmpty()) {
      return bookmarkService.getBookmarks(page);
    }

    return bookmarkService.searchBookmarks(query, page);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookmarkDTO createBookmark(@RequestBody @Valid CreateBookmarkRequest request) {
    return bookmarkService.createBookmark(request);
  }

}
