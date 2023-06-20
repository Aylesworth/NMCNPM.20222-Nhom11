package com.aylesw.mch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "child_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false)
    private String sex;

    private String ethnicity;

    private String birthplace;

    private String insuranceId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    @Column(columnDefinition = "timestamp")
    private Timestamp timestamp;
}
