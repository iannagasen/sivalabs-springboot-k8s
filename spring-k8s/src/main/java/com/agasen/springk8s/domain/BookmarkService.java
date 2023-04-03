package com.agasen.springk8s.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
  private final BookmarkRepository bookmarkRepository;

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
  public BookmarkDTO getBookmarks(int page) {
    int pageNo = page < 1 ? 0 : page - 1;
    Pageable pageable = PageRequest.of(pageNo, 5, Sort.Direction.DESC, "createdAt");
    Page<Bookmark> bookmarkPage = bookmarkRepository.findAll(pageable);
    return new BookmarkDTO(bookmarkPage);
  }
}