package io.github.aylesw.mch.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
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

    @JsonIgnore
    @Column(columnDefinition = "timestamp")
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private User parent;

    @JsonIgnore
    public long getAgeInDays() {
        return getAgeInDaysAsOf(LocalDate.now());
    }

    public long getAgeInMonths() {
        YearMonth start = YearMonth.from(dob.toLocalDate());
        YearMonth end = YearMonth.now();

        return start.until(end, ChronoUnit.MONTHS);
    }

    public long getAgeInDaysAsOf(LocalDate date) {
        return ChronoUnit.DAYS.between(dob.toLocalDate(), date);
    }
}
