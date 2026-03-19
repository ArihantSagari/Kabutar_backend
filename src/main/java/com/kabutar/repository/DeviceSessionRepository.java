package com.kabutar.repository;

import com.kabutar.entity.DeviceSession;
import com.kabutar.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for device session operations.
 */
@Repository
public interface DeviceSessionRepository extends JpaRepository<DeviceSession, UUID> {

    /**
     * Find active session for user
     */
    Optional<DeviceSession> findByUser(User user);

    /**
     * Find session by refresh token
     */
    Optional<DeviceSession> findByRefreshToken(String refreshToken);

    /**
     * Delete sessions when user logs in again
     */
    void deleteByUser(User user);
    
}