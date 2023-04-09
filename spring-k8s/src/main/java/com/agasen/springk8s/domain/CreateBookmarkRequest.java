package com.agasen.springk8s.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookmarkRequest {

  @NotEmpty(message = "TItle should not be empty")
  private String title;

  @NotEmpty(message = "Url should not be empty")
  private String url;

}
