package io.github.aylesw.mch.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "email_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "varchar(1024)")
    private String message;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp time;

    @Column(nullable = false)
    private Boolean sent;
}
