package mch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mch.dto.ChildDto;
import mch.dto.mapper.ChildDtoMapper;
import mch.repository.ChildRepository;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

	private final ChildRepository childRepository;
	private final ChildDtoMapper childDtoMapper;

	@Override
	public List<ChildDto> getAllChildren() {
		return childRepository.findAll().stream().map(child -> childDtoMapper.toDto(child))
				.collect(Collectors.toList());
	}

	@Override
	public List<ChildDto> getChildrenOfUser(Long userId) {
		return childRepository.findByParent(userId).stream().map(child -> childDtoMapper.toDto(child))
				.collect(Collectors.toList());
	}

	@Override
	public ChildDto getChildById(Long id) {
		return childDtoMapper.toDto(childRepository.findById(id).orElseThrow());
	}

	@Override
	public void addChild(ChildDto childDto) {
		childDto.setId(null);
		childRepository.save(childDtoMapper.toEntity(childDto));
	}

	@Override
	public void updateChild(Long id, ChildDto childDto) {
		childRepository.findById(id).orElseThrow();
		childDto.setId(id);
		childRepository.save(childDtoMapper.toEntity(childDto));
	}

}
