package org.vs.notesbe.controller;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vs.notesbe.dto.ApiResponse;
import org.vs.notesbe.dto.DeleteMultipleRequestDto;
import org.vs.notesbe.dto.NoteRequestDto;
import org.vs.notesbe.dto.NoteResponseDto;
import org.vs.notesbe.exception.AddNoteException;
import org.vs.notesbe.service.NoteService;
import org.vs.notesbe.util.JwtUtils;
import org.vs.notesbe.util.UserValidations;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(NoteController.class);

  private final UserValidations userValidations;
  private final NoteService noteService;
  private final JwtUtils jwtUtils;

  public NoteController(UserValidations userValidations, NoteService noteService, JwtUtils jwtUtils) {
    this.userValidations = userValidations;
    this.noteService = noteService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/addNote")
  public ResponseEntity<ApiResponse<NoteResponseDto>> addNote(
      @CookieValue String token,
      @RequestParam UUID userId,
      @RequestBody NoteRequestDto addNoteRequestDto
  ) {
    try {
      log.info("NOTE_CONTROLLER - ADD_NOTE ::: Received request to add note with cookie: {}", token);

      String uIdString = jwtUtils.extractUserId(token);

      ResponseEntity<ApiResponse<NoteResponseDto>> validationResponse = userValidations.validateUserToken(token, uIdString);
      if (validationResponse != null) {
        return validationResponse;
      }

      NoteResponseDto addedNote = noteService.addNote(userId, addNoteRequestDto);

      log.info("NOTE_CONTROLLER - ADD_NOTE ::: Note added successfully with ID: {}", addedNote.getNoteId());

      ApiResponse<NoteResponseDto> response = new ApiResponse<>(true, "Note added successfully", addedNote);

      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(response);
    } catch (Exception e) {
      log.error("NOTE_CONTROLLER - ADD_NOTE ::: Failed to add note: {}", e.getMessage());

      throw new AddNoteException("Failed to add note: " + e.getMessage());
    }
  }

  @GetMapping("/getNotes")
  public ResponseEntity<ApiResponse<List<NoteResponseDto>>> getNotes(
      @CookieValue String token,
      @RequestParam UUID userId
  ) {
    try {
      log.info("NOTE_CONTROLLER - GET_NOTES ::: Received request to get notes with cookie: {}", token);

      String uIdString = jwtUtils.extractUserId(token);

      ResponseEntity<ApiResponse<List<NoteResponseDto>>> validationResponse = userValidations.validateUserToken(token, uIdString);
      if (validationResponse != null) {
        return validationResponse;
      }

      List<NoteResponseDto> notes = noteService.getNotes(userId);

      log.info("NOTE_CONTROLLER - GET_NOTES ::: Notes retrieved successfully for user ID: {}", userId);

      ApiResponse<List<NoteResponseDto>> response = new ApiResponse<>(true, "Notes retrieved successfully", notes);

      return ResponseEntity
          .status(HttpStatus.OK)
          .body(response);
    } catch (Exception e) {
      log.error("NOTE_CONTROLLER - GET_NOTES ::: Failed to retrieve notes: {}", e.getMessage());

      throw new AddNoteException("Failed to retrieve notes: " + e.getMessage());
    }
  }

  @GetMapping("/getNote")
  public ResponseEntity<ApiResponse<NoteResponseDto>> getNote(
      @CookieValue String token,
      @RequestParam UUID userId,
      @RequestParam UUID noteId
  ) {
    try {
      log.info("NOTE_CONTROLLER - GET_NOTE ::: Received request to get note with ID for user {}: {}", noteId, userId);

      String uIdString = jwtUtils.extractUserId(token);

      ResponseEntity<ApiResponse<NoteResponseDto>> validationResponse = userValidations.validateUserToken(token, uIdString);
      if (validationResponse != null) {
        return validationResponse;
      }

      NoteResponseDto noteResponse = noteService.getNote(userId, noteId);

      log.info("NOTE_CONTROLLER - GET_NOTE ::: Note retrieved successfully with ID: {}", noteResponse.getNoteId());

      ApiResponse<NoteResponseDto> response = new ApiResponse<>(true, "Note retrieved successfully", noteResponse);

      return ResponseEntity
          .status(HttpStatus.OK)
          .body(response);
    } catch (Exception e) {
      log.error("NOTE_CONTROLLER - GET_NOTE ::: Failed to retrieve note: {}", e.getMessage());

      throw new AddNoteException("Failed to retrieve note: " + e.getMessage());
    }
  }

  @PutMapping("/editNote")
  public ResponseEntity<ApiResponse<NoteResponseDto>> editNote(
      @CookieValue String token,
      @RequestParam UUID userId,
      @RequestParam UUID noteId,
      @RequestBody NoteRequestDto editNoteRequestDto
  ) {
    try {
      log.info("NOTE_CONTROLLER - EDIT_NOTE ::: Received request to edit note with cookie: {}", token);

      String uIdString = jwtUtils.extractUserId(token);

      ResponseEntity<ApiResponse<NoteResponseDto>> validationResponse = userValidations.validateUserToken(token, uIdString);
      if (validationResponse != null) {
        return validationResponse;
      }

      NoteResponseDto editedNote = noteService.editNote(userId, noteId, editNoteRequestDto);

      log.info("NOTE_CONTROLLER - EDIT_NOTE ::: Note edited successfully with ID: {}", editedNote.getNoteId());

      ApiResponse<NoteResponseDto> response = new ApiResponse<>(true, "Note edited successfully", editedNote);

      return ResponseEntity
          .status(HttpStatus.OK)
          .body(response);
    } catch (Exception e) {
      log.error("NOTE_CONTROLLER - EDIT_NOTE ::: Failed to edit note: {}", e.getMessage());

      throw new AddNoteException("Failed to edit note: " + e.getMessage());
    }
  }

  @DeleteMapping("/deleteNote")
  public ResponseEntity<ApiResponse<Object>> deleteNote(
      @CookieValue String token,
      @RequestParam UUID userId,
      @RequestParam UUID noteId
  ) {
    try {
      log.info("NOTE_CONTROLLER - DELETE_NOTE ::: Received request to delete note with ID for user {}: {}", noteId, userId);

      String uIdString = jwtUtils.extractUserId(token);

      ResponseEntity<ApiResponse<Object>> validationResponse = userValidations.validateUserToken(token, uIdString);
      if (validationResponse != null) {
        return validationResponse;
      }

      noteService.deleteNote(userId, noteId);

      log.info("NOTE_CONTROLLER - DELETE_NOTE ::: Note deleted successfully with ID: {}", noteId);

      ApiResponse<Object> response = new ApiResponse<>(true, "Note deleted successfully", null);

      return ResponseEntity
          .status(HttpStatus.OK)
          .body(response);
    } catch (Exception e) {
      log.error("NOTE_CONTROLLER - DELETE_NOTE ::: Failed to delete note: {}", e.getMessage());

      throw new AddNoteException("Failed to delete note: " + e.getMessage());
    }
  }

  @PostMapping("/deleteMultipleNotes")
  public ResponseEntity<ApiResponse<Object>> deleteMultipleNotes(
      @CookieValue String token,
      @RequestParam UUID userId,
      @RequestBody DeleteMultipleRequestDto deleteMultipleRequestDto
  ) {
    try {
      log.info("NOTE_CONTROLLER - DELETE_MULTIPLE_NOTES ::: Received request to delete multiple notes for user ID: {}", userId);

      String uIdString = jwtUtils.extractUserId(token);

      ResponseEntity<ApiResponse<Object>> validationResponse = userValidations.validateUserToken(token, uIdString);
      if (validationResponse != null) {
        return validationResponse;
      }

      noteService.deleteMultipleNotes(userId, deleteMultipleRequestDto);

      log.info("NOTE_CONTROLLER - DELETE_MULTIPLE_NOTES ::: Multiple notes deleted successfully for user ID: {}", userId);

      ApiResponse<Object> response = new ApiResponse<>(true, "Multiple notes deleted successfully", null);

      return ResponseEntity
          .status(HttpStatus.OK)
          .body(response);
    } catch (Exception e) {
      log.error("NOTE_CONTROLLER - DELETE_MULTIPLE_NOTES ::: Failed to delete multiple notes: {}", e.getMessage());

      throw new AddNoteException("Failed to delete multiple notes: " + e.getMessage());
    }
  }

}
