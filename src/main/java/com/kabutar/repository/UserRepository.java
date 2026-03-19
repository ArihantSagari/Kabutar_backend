package com.kabutar.repository;

import com.kabutar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User database operations.
 *
 * Spring automatically implements the methods below.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by their login ID (@username)
     */
    Optional<User> findByUserId(String userId);

    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a username already exists
     */
    boolean existsByUserId(String userId);

    /**
     * Check if an email already exists
     */
    boolean existsByEmail(String email);

}