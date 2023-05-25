package mch.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
	@NotEmpty
	@Pattern(regexp = "[A-Za-z0-9.]+[@][A-Za-z0-9]+([.][A-Za-z0-9]+)+")
	private String email;
	
	@NotEmpty
	@Size(min = 8)
	private String password;
	
	@NotEmpty
	private String fullName;
	
	@NotNull
	private Date dob;
	
	@NotEmpty
	private String sex;
	
	@NotEmpty
	private String phoneNumber;
	
	@NotEmpty
	private String address;
}
