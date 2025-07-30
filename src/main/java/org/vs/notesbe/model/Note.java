package org.vs.notesbe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Data
@Entity
@Table(name = "notes")
public class Note {

  @Id
  @GeneratedValue
  @Column(name = "n_id", nullable = false, unique = true)
  private UUID noteId;

  @Column(name = "u_id", nullable = false)
  private UUID userId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "body", nullable = false)
  private String body;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private String createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private String updatedAt;
}
