package io.github.aylesw.mch.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "examination")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date date;

    private String facility;

    private String reason;

    private String result;

    private String note;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "prescription",
            joinColumns = @JoinColumn(name = "examination_id"),
            inverseJoinColumns = @JoinColumn(name = "medicine_id")
    )
    private List<Medicine> medicines;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

}
