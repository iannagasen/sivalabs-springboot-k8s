package com.agasen.springk8s.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.agasen.springk8s.domain.Bookmark;
import com.agasen.springk8s.domain.BookmarkRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookmarkControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private BookmarkRepository bookmarkRepository;

  private List<Bookmark> bookmarks;

  @BeforeEach
  void setUp() {
    // before starting any test we are simply deleting all the records
    bookmarkRepository.deleteAllInBatch();

    bookmarks = new ArrayList<>();
    bookmarks.add(new Bookmark(null, "SivaLabs", "https://sivalabs.in", Instant.now()));
    bookmarks.add(new Bookmark(null, "SpringBlog", "https://spring.io/blog", Instant.now()));
    bookmarks.add(new Bookmark(null, "Quarkus", "https://quarkus.io/", Instant.now()));
    bookmarks.add(new Bookmark(null, "Micronaut", "https://micronaut.io/", Instant.now()));
    bookmarks.add(new Bookmark(null, "JOOQ", "https://www.jooq.org/", Instant.now()));
    bookmarks.add(new Bookmark(null, "VladMihalcea", "https://vladmihalcea.com", Instant.now()));
    bookmarks.add(new Bookmark(null, "Thoughts On Java", "https://thorben-janssen.com", Instant.now()));
    bookmarks.add(new Bookmark(null, "DZone", "https://dzone.com", Instant.now()));
    bookmarks.add(new Bookmark(null, "DevOpsBookmarks", "https://www.devopsbookmarks.com/", Instant.now()));

    bookmarkRepository.saveAll(bookmarks);
  }

  @Test
  void shouldGetBookmarks() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/api/bookmarks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements", CoreMatchers.equalTo(bookmarks.size())))
        .andExpect(jsonPath("$.totalPages", CoreMatchers.equalTo(bookmarks.size() / 5 + 1)))
        .andExpect(jsonPath("$.isFirst", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.isLast", CoreMatchers.equalTo(false)))
        .andExpect(jsonPath("$.hasNext", CoreMatchers.equalTo(true)))
        .andExpect(jsonPath("$.hasPrevious", CoreMatchers.equalTo(false)));
  }
}
