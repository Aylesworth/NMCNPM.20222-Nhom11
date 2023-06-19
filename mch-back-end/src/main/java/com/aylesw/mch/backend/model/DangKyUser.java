package com.aylesw.mch.backend.model;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "dang_ky_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DangKyUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	@Size(min = 8)
	private String password;
	
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

	@Column(columnDefinition = "timestamp")
	private Timestamp timestamp;
}
