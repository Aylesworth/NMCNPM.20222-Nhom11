package mch.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private Long id;
	private String email;
	private String fullName;
	private String citizenId;
	private Date dob;
	private String sex;
	private String phoneNumber;
	private String address;
	private String insuranceId;
}
