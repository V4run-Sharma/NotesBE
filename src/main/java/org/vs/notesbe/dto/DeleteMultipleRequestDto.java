package org.vs.notesbe.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteMultipleRequestDto {
    private List<UUID> noteIds;
}
