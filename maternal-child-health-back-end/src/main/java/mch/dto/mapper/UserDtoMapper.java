package mch.dto.mapper;

import org.springframework.stereotype.Component;

import mch.dto.UserDto;
import mch.model.User;

@Component
public class UserDtoMapper {
	public UserDto toDto(User user) {
		UserDto dto = UserDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.fullName(user.getFullName())
				.citizenId(user.getCitizenId())
				.dob(user.getDob())
				.sex(user.getSex())
				.phoneNumber(user.getPhoneNumber())
				.address(user.getAddress())
				.insuranceId(user.getInsuranceId())
				.build();
		return dto;
	}
	
	public User merge(User user, UserDto userDto) {
		user.setFullName(userDto.getFullName());
		user.setCitizenId(userDto.getCitizenId());
		user.setDob(userDto.getDob());
		user.setSex(userDto.getSex());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setAddress(userDto.getAddress());
		user.setInsuranceId(userDto.getInsuranceId());
		return user;
	}
}
