package org.vs.notesbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.notesbe.model.User;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    User findByUserName(String userName);

    User findByEmail(String email);
}
