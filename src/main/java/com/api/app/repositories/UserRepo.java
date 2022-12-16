package com.api.app.repositories;

import com.api.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> findByCreatedBy(User user);
    List<User> findByCreatedAt(Date date);

    List<User> findByCreatedAtBetween(Date startCreatedAt, Date endCreatedAt);

    // Numbers use LessThan and GreaterThan
    List<User> findByCreatedAtLessThanEqual(Date createdAt);

    List<User> findByCreatedAtBefore(Date createdAt);

    List<User> findByCreatedAtAfter(Date createdAt);

    void deleteByUsername(String username);

    Boolean existsByUsername(String username);
}
