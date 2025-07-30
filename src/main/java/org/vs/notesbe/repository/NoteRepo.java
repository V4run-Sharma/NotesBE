package org.vs.notesbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.notesbe.model.Note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepo extends JpaRepository<Note, String> {
  List<Note> findAllByUserId(UUID userId);

  Optional<Note> findByUserIdAndNoteId(UUID userId, UUID noteId);
}
