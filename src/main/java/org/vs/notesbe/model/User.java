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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue
  @Column(name = "u_id", nullable = false, unique = true)
  private UUID userId;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private String createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private String updatedAt;

  @Override
  public String toString() {
    return "User{" +
        "userId=" + userId +
        ", userName='" + userName + '\'' +
        ", email='" + email + '\'' +
        ", createdAt='" + createdAt + '\'' +
        ", updatedAt='" + updatedAt + '\'' +
        '}';
  }
}
