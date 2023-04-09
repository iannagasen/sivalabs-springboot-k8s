package com.agasen.springk8s.domain;

import java.time.Instant;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
  private final BookmarkRepository bookmarkRepository;
  private final BookmarkMapper bookmarkMapper;

  /**
   * What is @Transactional(readOnly = true)?
   * - this is to improve performance, since read-only transactions dont need to
   * acquire locks on the database,
   * which can reduce contention and improve concurrency
   * - exception will be thrown if you try to modify data within a read-only
   * transaction
   * By default, transactions are read-write
   * 
   * retrieved based on order by "createdAt" property
   */
  @Transactional(readOnly = true)
  public BookmarksDTO getBookmarks(int page) {
    int pageNo = page < 1 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(pageNo, 5, Sort.Direction.DESC, "createdAt");
    // Page<BookmarkDTO> bookmarkPage =
    // bookmarkRepository.findAll(pageable).map(bookmarkMapper);
    Page<BookmarkDTO> bookmarkPage = bookmarkRepository.findBookmarks(pageable);
    return new BookmarksDTO(bookmarkPage);
  }

  @Transactional(readOnly = true)
  public BookmarksDTO searchBookmarks(String query, int page) {
    int pageNo = page < 1 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(pageNo, 5, Sort.Direction.DESC, "createdAt");
    Page<BookmarkDTO> bookmarkPage = bookmarkRepository.findByTitleContainsIgnoreCase(query, pageable);
    return new BookmarksDTO(bookmarkPage);
  }

  public BookmarkDTO createBookmark(@Valid CreateBookmarkRequest request) {
    var bookmark = new Bookmark(null, request.getTitle(), request.getUrl(), Instant.now());
    var savedBookmark = bookmarkRepository.save(bookmark);
    return bookmarkMapper.apply(savedBookmark);
  }

}
