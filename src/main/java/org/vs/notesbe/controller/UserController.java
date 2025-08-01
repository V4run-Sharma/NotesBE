package org.vs.notesbe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vs.notesbe.dto.ApiResponse;
import org.vs.notesbe.dto.UserSignInRequestDto;
import org.vs.notesbe.dto.UserSignUpRequestDto;
import org.vs.notesbe.exception.SigninException;
import org.vs.notesbe.exception.SignupException;
import org.vs.notesbe.model.User;
import org.vs.notesbe.service.UserService;
import org.vs.notesbe.util.JwtUtils;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;
  private final JwtUtils jwtUtils;

  public UserController(UserService userService, JwtUtils jwtUtils) {
    this.userService = userService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<User>> signup(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
    try {
      log.info("USER_CONTROLLER - SIGNUP ::: Received signup request for user: {}", userSignUpRequestDto.getUserName());
      var user = userService.signup(userSignUpRequestDto);
      log.info("USER_CONTROLLER - SIGNUP ::: User signed up successfully: {}", user.getUserName());

      ApiResponse<User> response = new ApiResponse<>(true, "Signup successful", null);

      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(response);
    } catch (Exception e) {
      log.error("USER_CONTROLLER - SIGNUP ::: Signup failed for user {}: {}", userSignUpRequestDto.getUserName(), e.getMessage());

      throw new SignupException("Signup failed: " + e.getMessage());
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse<String>> signin(@RequestBody UserSignInRequestDto userSignInRequestDto) {
    try {
      log.info("USER_CONTROLLER - SIGNIN ::: Received signin request for user: {}", userSignInRequestDto.getUserName());
      User user = userService.signin(userSignInRequestDto);
      log.info("USER_CONTROLLER - SIGNIN ::: User signed in successfully");

      String token = jwtUtils.generateToken(user.getUserId());
      ResponseCookie cookie = ResponseCookie.from("token", token)
          .sameSite("Lax")
          .maxAge(360000) // 10 hours
          .httpOnly(true)
          .secure(true)
          .path("/")
          .build();

      ApiResponse<String> response = new ApiResponse<>(true, "Signin successful", user.getUserName());
      log.info("USER_CONTROLLER - SIGNIN ::: Generated cookie {}", cookie);
      return ResponseEntity
          .status(HttpStatus.OK)
          .header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body(response);
    } catch (Exception e) {
      log.error("USER_CONTROLLER - SIGNIN ::: Signin failed for user {}: {}", userSignInRequestDto.getUserName(), e.getMessage());

      throw new SigninException("Signin failed: " + e.getMessage());
    }
  }
}
