package com.api.app.repositories;

import com.api.app.models.Role;
import com.api.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    List<Role> findByCreatedBy(User user);
    List<Role> findByCreatedAt(Date date);

    List<Role> findByCreatedAtBetween(Date startCreatedAt, Date endCreatedAt);

    // Numbers use LessThan and GreaterThan
    List<Role> findByCreatedAtLessThanEqual(Date createdAt);

    List<Role> findByCreatedAtBefore(Date createdAt);

    List<Role> findByCreatedAtAfter(Date createdAt);
}
