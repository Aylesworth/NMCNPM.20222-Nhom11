package com.aylesw.mch.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "body_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Date date;

    private String note;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;
}
