package org.vs.notesbe.dto;

import lombok.Data;

@Data
public class UserSignInRequestDto {
    private String userName;
    private String password;
}
