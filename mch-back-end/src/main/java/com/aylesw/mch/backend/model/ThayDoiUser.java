package com.aylesw.mch.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "thay_doi_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThayDoiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String tenDayDu;

    @NotNull
    private Date ngaySinh;

    @NotEmpty
    private String gioiTinh;

    @NotEmpty
    private String sdt;

    @NotEmpty
    private String diaChi;

    private String cccd;

    private String bhyt;

    private Long userId;

    @Column(columnDefinition = "timestamp")
    private Timestamp timestamp;
}
