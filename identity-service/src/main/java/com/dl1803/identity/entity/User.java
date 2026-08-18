package com.dl1803.identity.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String password;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified;

    @Column(name = "verification_otp", columnDefinition = "VARCHAR(4)")
    String verificationOtp;

    @Column(name = "otp_expiry_time")
    LocalDateTime otpExpiryTime;

    @Column(name = "last_otp_sent_time")
    LocalDateTime lastOtpSentTime;

    @Column(name = "otp_attempt_count", columnDefinition = "INT DEFAULT 0")
    int otpAttemptCount;

    @ManyToMany
    Set<Role> roles;
}
