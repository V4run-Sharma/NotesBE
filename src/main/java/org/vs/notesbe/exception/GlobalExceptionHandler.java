package org.vs.notesbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.vs.notesbe.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(SigninException.class)
  public ResponseEntity<ApiResponse<Object>> handleSigninException(SigninException ex) {
    ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SignupException.class)
  public ResponseEntity<ApiResponse<Object>> handleSignupException(SignupException ex) {
    ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AddNoteException.class)
  public ResponseEntity<ApiResponse<Object>> handleAddNoteException(AddNoteException ex) {
    ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(GetNoteException.class)
  public ResponseEntity<ApiResponse<Object>> handleGetNoteException(GetNoteException ex) {
    ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)  // catch-all
  public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
    ApiResponse<Object> response = new ApiResponse<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
