package com.agasen.springk8s.api;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agasen.springk8s.domain.Bookmark;
import com.agasen.springk8s.domain.BookmarkDTO;
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
  public BookmarkDTO getBookmarks(@RequestParam(name = "page", defaultValue = "1") int page) {
    return bookmarkService.getBookmarks(page);
  }

}
