package com.kabutar.service;

import com.kabutar.entity.LoginAttempt;
import com.kabutar.repository.LoginAttemptRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**

* Handles login security protections.
*
* Features:
* * Brute force protection
* * Account lock after repeated failures
* * Automatic unlock after lock duration
    */
    @Service
    public class LoginSecurityService {

  private final LoginAttemptRepository loginAttemptRepository;

  /** Maximum allowed failed attempts */
  private static final int MAX_ATTEMPTS = 5;

  /** Lock duration in minutes */
  private static final int LOCK_MINUTES = 15;

  public LoginSecurityService(LoginAttemptRepository loginAttemptRepository) {
  this.loginAttemptRepository = loginAttemptRepository;
  }

  /**

  * Check if the account is currently locked.
  *
  * This is called before password validation.
    */
    public void checkLock(String userId, String ipAddress) {

    LoginAttempt attempt = loginAttemptRepository
    .findByUserIdAndIpAddress(userId, ipAddress)
    .orElse(null);

    if (attempt == null) {
    return;
    }

    if (attempt.getLockedUntil() != null &&
    attempt.getLockedUntil().isAfter(LocalDateTime.now())) {

    
     throw new RuntimeException(
             "Too many failed login attempts. Please try again later."
     );
    

    }
    }

  /**

  * Record failed login attempt.
  *
  * Increases attempt counter and locks the account
  * when maximum attempts are exceeded.
    */
    @Transactional
    public void recordFailedAttempt(String userId, String ipAddress) {

    LoginAttempt attempt = loginAttemptRepository
    .findByUserIdAndIpAddress(userId, ipAddress)
    .orElseGet(() -> {

    
             LoginAttempt newAttempt = new LoginAttempt();
             newAttempt.setUserId(userId);
             newAttempt.setIpAddress(ipAddress);
             newAttempt.setAttempts(0);

             return newAttempt;
         });
   

    int newAttempts = attempt.getAttempts() + 1;

    attempt.setAttempts(newAttempts);
    attempt.setLastAttempt(LocalDateTime.now());

    if (newAttempts >= MAX_ATTEMPTS) {

   
     attempt.setLockedUntil(
             LocalDateTime.now().plusMinutes(LOCK_MINUTES)
     );
    

    }

    loginAttemptRepository.save(attempt);
    }

  /**

  * Reset failed attempts after successful login.
    */
    @Transactional
    public void resetAttempts(String userId, String ipAddress) {

    loginAttemptRepository
    .findByUserIdAndIpAddress(userId, ipAddress)
    .ifPresent(loginAttemptRepository::delete);
    }
    }
