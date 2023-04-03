package com.agasen.springk8s.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agasen.springk8s.domain.BookmarksDTO;
import com.agasen.springk8s.domain.BookmarkService;

import lombok.RequiredArgsConstructor;

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
  public BookmarksDTO getBookmarks(@RequestParam(name = "page", defaultValue = "1") int page) {
    return bookmarkService.getBookmarks(page);
  }

}
