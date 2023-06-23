package io.github.aylesw.mch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "age_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgeGroup {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer minAgeInDays;

    @Column(nullable = false)
    private Integer maxAgeInDays;
}
