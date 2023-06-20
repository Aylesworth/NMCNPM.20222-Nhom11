package com.aylesw.mch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "user_change")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false)
    private String sex;

    private String phoneNumber;

    private String address;

    private String citizenId;

    private String insuranceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "timestamp")
    private Timestamp timestamp;
}
