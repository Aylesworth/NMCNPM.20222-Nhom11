package mch.dto.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mch.dto.ChildDto;
import mch.model.Child;
import mch.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ChildDtoMapper {
	
	private final UserRepository userRepository;
	
	public ChildDto toDto(Child child) {
		return ChildDto.builder()
				.id(child.getId())
				.fullName(child.getFullName())
				.nickname(child.getNickname())
				.dob(child.getDob())
				.sex(child.getSex())
				.ethnicity(child.getEthnicity())
				.nationality(child.getNationality())
				.birthplace(child.getBirthplace())
				.insuranceId(child.getInsuranceId())
				.parentId(child.getParent().getId())
				.build();
	}
	
	public Child toEntity(ChildDto dto) {
		return Child.builder()
				.id(dto.getId())
				.fullName(dto.getFullName())
				.nickname(dto.getNickname())
				.dob(dto.getDob())
				.sex(dto.getSex())
				.ethnicity(dto.getEthnicity())
				.nationality(dto.getNationality())
				.birthplace(dto.getBirthplace())
				.insuranceId(dto.getInsuranceId())
				.parent(userRepository.findById(dto.getParentId()).orElseThrow())
				.build();
	}
}
