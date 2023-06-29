package io.github.aylesw.mch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "injection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Injection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "child_id")
    private Child child;

    @ManyToOne @JoinColumn(name = "vaccine_id")
    private Vaccine vaccine;

    @Column(nullable = false)
    private Date date;

    private String note;

    @OneToMany(mappedBy = "injection", cascade = CascadeType.REMOVE)
    private List<Reaction> reactions;

    private String status;
}
