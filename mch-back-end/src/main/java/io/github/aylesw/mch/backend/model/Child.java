package io.github.aylesw.mch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "child")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Child {
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

    public long getAgeInDays() {
        return getAgeInDaysAsOf(LocalDate.now());
    }

    public long getAgeInDaysAsOf(LocalDate date) {
        return ChronoUnit.DAYS.between(dob.toLocalDate(), date);
    }
}
