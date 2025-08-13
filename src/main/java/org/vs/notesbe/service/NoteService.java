package org.vs.notesbe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.vs.notesbe.dto.DeleteMultipleRequestDto;
import org.vs.notesbe.dto.NoteRequestDto;
import org.vs.notesbe.dto.NoteResponseDto;
import org.vs.notesbe.exception.GetNoteException;
import org.vs.notesbe.model.Note;
import org.vs.notesbe.repository.NoteRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepo noteRepo;

    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public NoteResponseDto addNote(UUID userId, NoteRequestDto addNoteRequestDto) {
        log.info("NOTE_SERVICE - ADD_NOTE ::: Adding note with title: {}", addNoteRequestDto.getTitle());

        Note note = new Note();
        note.setUserId(userId);
        note.setTitle(addNoteRequestDto.getTitle());
        note.setBody(addNoteRequestDto.getBody());

        noteRepo.save(note);

        NoteResponseDto noteResponseDto = new NoteResponseDto();
        BeanUtils.copyProperties(note, noteResponseDto);

        log.info("NOTE_SERVICE - ADD_NOTE ::: Note added successfully with ID: {}", noteResponseDto.getNoteId());

        return noteResponseDto;
    }

    public List<NoteResponseDto> getNotes(UUID userId) {
        log.info("NOTE_SERVICE - GET_NOTES ::: Fetching notes for user ID: {}", userId);

        List<NoteResponseDto> noteResponseDtos = noteRepo.findAllByUserId(userId).stream()
                .map(note -> {
                    NoteResponseDto dto = new NoteResponseDto();
                    BeanUtils.copyProperties(note, dto);
                    return dto;
                })
                .toList();

        log.info("NOTE_SERVICE - GET_NOTES ::: Found {} notes for user ID: {}", noteResponseDtos.size(), userId);

        return noteResponseDtos;
    }

    public NoteResponseDto getNote(UUID userId, UUID noteId) {
        log.info("NOTE_SERVICE - GET_NOTE ::: Fetching note with ID for user {}: {}", noteId, userId);

        Optional<Note> existingNoteOpt = noteRepo.findByUserIdAndNoteId(userId, noteId);
        if (existingNoteOpt.isEmpty()) {
            log.error("NOTE_SERVICE - GET_NOTE ::: Note with ID {} not found for user {}", noteId, userId);
            throw new GetNoteException("Note not found");
        }

        NoteResponseDto noteResponseDto = new NoteResponseDto();
        BeanUtils.copyProperties(existingNoteOpt.get(), noteResponseDto);

        return noteResponseDto;
    }

    public NoteResponseDto editNote(UUID userId, UUID noteId, NoteRequestDto editNoteRequestDto) {
        log.info("NOTE_SERVICE - EDIT_NOTE ::: Editing note with ID: {} for user ID: {}", noteId, userId);

        Optional<Note> existingNoteOpt = noteRepo.findByUserIdAndNoteId(userId, noteId);
        if (existingNoteOpt.isEmpty()) {
            log.error("NOTE_SERVICE - EDIT_NOTE ::: Note with ID {} not found for user {}", noteId, userId);
            throw new GetNoteException("Note not found");
        }

        Note existingNote = existingNoteOpt.get();
        existingNote.setTitle(editNoteRequestDto.getTitle());
        existingNote.setBody(editNoteRequestDto.getBody());

        noteRepo.save(existingNote);

        NoteResponseDto noteResponseDto = new NoteResponseDto();
        BeanUtils.copyProperties(existingNote, noteResponseDto);

        log.info("NOTE_SERVICE - EDIT_NOTE ::: Note with ID {} edited successfully", noteId);

        return noteResponseDto;
    }

    public void deleteNote(UUID userId, UUID noteId) {
        log.info("NOTE_SERVICE - DELETE_NOTE ::: Deleting note with ID: {} for user ID: {}", noteId, userId);

        Optional<Note> existingNoteOpt = noteRepo.findByUserIdAndNoteId(userId, noteId);
        if (existingNoteOpt.isEmpty()) {
            log.error("NOTE_SERVICE - DELETE_NOTE ::: Note with ID {} not found for user {}", noteId, userId);
            throw new GetNoteException("Note not found");
        }

        noteRepo.delete(existingNoteOpt.get());

        log.info("NOTE_SERVICE - DELETE_NOTE ::: Note with ID {} deleted successfully", noteId);
    }

    public void deleteMultipleNotes(UUID userId, DeleteMultipleRequestDto deleteMultipleRequestDto) {
        log.info("NOTE_SERVICE - DELETE_MULTIPLE_NOTES ::: Deleting notes for user ID: {}", userId);

        List<Note> notesToDelete = noteRepo.findAllByUserId(userId).stream()
                .filter(note -> deleteMultipleRequestDto.getNoteIds().contains(note.getNoteId()))
                .toList();

        if (notesToDelete.isEmpty()) {
            log.warn("NOTE_SERVICE - DELETE_MULTIPLE_NOTES ::: No notes found to delete for user ID: {}", userId);
            return;
        }

        noteRepo.deleteAll(notesToDelete);

        log.info("NOTE_SERVICE - DELETE_MULTIPLE_NOTES ::: Successfully deleted {} notes for user ID: {}", notesToDelete.size(), userId);
    }

}
