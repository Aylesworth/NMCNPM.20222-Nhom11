package io.github.aylesw.mch.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vaccine")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer doseNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_group")
    private AgeGroup ageGroup;
}
