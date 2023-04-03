package com.agasen.springk8s.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  @Query("""
      select
        new com.agasen.springk8s.domain.BookmarkDTO(b.id, b.title, b.url, b.createdAt)
      from Bookmark b
      """)
  Page<BookmarkDTO> findBookmarks(Pageable pageable);
}
