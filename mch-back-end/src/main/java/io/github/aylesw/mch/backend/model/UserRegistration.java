package io.github.aylesw.mch.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "user_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	private Date dob;

	@Column(nullable = false)
	private String sex;

	private String phoneNumber;

	private String address;

	private String citizenId;

	private String insuranceId;

	@Column(columnDefinition = "timestamp")
	private Timestamp requested;

	@Column(columnDefinition = "timestamp")
	private Timestamp approved;
}
