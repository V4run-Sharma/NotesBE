package org.vs.notesbe.dto;

import lombok.Data;

@Data
public class UserSignUpRequestDto {
  private String userName;
  private String email;
  private String password;
}
