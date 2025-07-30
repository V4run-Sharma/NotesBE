package org.vs.notesbe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vs.notesbe.dto.UserSignInRequestDto;
import org.vs.notesbe.dto.UserSignUpRequestDto;
import org.vs.notesbe.exception.SigninException;
import org.vs.notesbe.exception.SignupException;
import org.vs.notesbe.model.User;
import org.vs.notesbe.repository.UserRepo;

@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  public User signup(UserSignUpRequestDto requestDto) {
    if (userRepo.findByEmail(requestDto.getEmail()) != null) {
      log.error("USER_SERVICE - SIGNUP ::: Signup failed: User already exists with email {}", requestDto.getEmail());
      throw new SignupException("User already exists");
    }
    if (userRepo.findByUserName(requestDto.getUserName()) != null) {
      log.error("USER_SERVICE - SIGNUP ::: Signup failed: User already exists with username {}", requestDto.getUserName());
      throw new SignupException("User already exists");
    }

    User user = new User();
    user.setUserName(requestDto.getUserName());
    user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
    user.setEmail(requestDto.getEmail());

    userRepo.save(user);

    log.info("USER_SERVICE - SIGNUP ::: User {} signed up successfully", user.getUserName());

    return user;
  }

  public User signin(UserSignInRequestDto requestDto) {
    User user = userRepo.findByUserName(requestDto.getUserName());
    if (user == null) {
      log.error("USER_SERVICE - SIGNIN ::: Signin failed: Invalid username or password for user {}", requestDto.getUserName());
      throw new SigninException("Invalid username or password");
    }
    boolean passwordMatches = passwordEncoder.matches(requestDto.getPassword(), user.getPassword());
    if (!passwordMatches) {
      log.error("USER_SERVICE - SIGNIN ::: Signin failed: Invalid username or password for user {}", requestDto.getUserName());
      throw new SigninException("Invalid username or password");
    }

    log.info("USER_SERVICE - SIGNIN ::: User {} signed in successfully", user.getUserName());

    return user;
  }
}
