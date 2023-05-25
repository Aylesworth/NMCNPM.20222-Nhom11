package mch.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildDto {
	
	private Long id;
	
	@NotEmpty
	private String fullName;

	private String nickname;

	@NotNull
	private Date dob;

	@NotEmpty
	private String sex;

	private String ethnicity;

	private String nationality;

	private String birthplace;

	@Pattern(regexp = "[A-Z0-9]{12,18}")
	private String insuranceId;

	@NotNull
	private Long parentId;
}
