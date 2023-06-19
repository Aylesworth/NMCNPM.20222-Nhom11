package com.aylesw.mch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "dang_ky_tre_em")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DangKyTreEm {
    @Id
    @GeneratedValue
    private Long id;

    private String tenDayDu;

    private Date ngaySinh;

    private String gioiTinh;

    private String danToc;

    private String noiSinh;

    private String bhyt;

    private Long parentId;

    @Column(columnDefinition = "timestamp")
    private Timestamp timestamp;
}
