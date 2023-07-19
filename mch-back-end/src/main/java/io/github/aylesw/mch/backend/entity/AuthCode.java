package io.github.aylesw.mch.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "auth_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "char(6)")
    private String value;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String usedFor;

    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp expiresAt;

    @Column(nullable = false)
    private Boolean used;
}
