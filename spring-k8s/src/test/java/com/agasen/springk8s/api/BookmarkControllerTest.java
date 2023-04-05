package com.agasen.springk8s.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.agasen.springk8s.domain.Bookmark;
import com.agasen.springk8s.domain.BookmarkRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(/* webEnvironment = WebEnvironment.RANDOM_PORT */)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.7:///databasename"
})
@Testcontainers
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

  @ParameterizedTest
  @CsvSource({
      "1,9,2,1,true,false,true,false",
      "2,9,2,2,false,true,false,true"
  })
  void shouldGetBookmarks(int pageNo, int totalElements, int totalPages, int currentPage, boolean isFirst,
      boolean isLast, boolean hasNext, boolean hasPrevious) throws Exception {

    mvc.perform(MockMvcRequestBuilders.get("/api/bookmarks?page=" + pageNo))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements", CoreMatchers.equalTo(totalElements)))
        .andExpect(jsonPath("$.totalPages", CoreMatchers.equalTo(totalPages)))
        .andExpect(jsonPath("$.currentPage", CoreMatchers.equalTo(currentPage)))
        .andExpect(jsonPath("$.isFirst", CoreMatchers.equalTo(isFirst)))
        .andExpect(jsonPath("$.isLast", CoreMatchers.equalTo(isLast)))
        .andExpect(jsonPath("$.hasNext", CoreMatchers.equalTo(hasNext)))
        .andExpect(jsonPath("$.hasPrevious", CoreMatchers.equalTo(hasPrevious)));
  }
}
