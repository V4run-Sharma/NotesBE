package org.vs.notesbe.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NoteResponseDto {
    private UUID noteId;
    private String title;
    private String body;
    private String updatedAt;
}
