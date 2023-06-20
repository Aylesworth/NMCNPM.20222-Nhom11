package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.ChildDto;
import com.aylesw.mch.backend.exception.ResourceNotFoundException;
import com.aylesw.mch.backend.model.Child;
import com.aylesw.mch.backend.model.ChildChange;
import com.aylesw.mch.backend.model.ChildRegistration;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.ChildChangeRepository;
import com.aylesw.mch.backend.repository.ChildRegistrationRepository;
import com.aylesw.mch.backend.repository.ChildRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import com.aylesw.mch.backend.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final ChildRegistrationRepository childRegistrationRepository;
    private final ChildChangeRepository childChangeRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public void approveChildRegistration(Long childRegistrationId) {
        ChildRegistration childRegistration = childRegistrationRepository.findById(childRegistrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Child registration", "id", childRegistrationId));

        Child child = mapper.map(childRegistration, Child.class);
        child.setId(null);

        childRepository.save(child);
    }

    @Override
    public void approveChildChange(Long childChangeId) {
        ChildChange childChange = childChangeRepository.findById(childChangeId)
                .orElseThrow(() -> new ResourceNotFoundException("Child change", "id", childChangeId));

        Child child = mapper.map(childChange, Child.class);
        child.setId(childChange.getChild().getId());
        child.setParent(childChange.getChild().getParent());

        childRepository.save(child);
    }

    @Override
    public List<ChildDto> search(String keyword) {
        return childRepository.findByKeyword(keyword).stream()
                .map(child -> mapper.map(child, ChildDto.class))
                .toList();
    }

    @Override
    public ChildDto getChild(Long childId) {
        return mapper.map(childRepository.findById(childId).orElseThrow(), ChildDto.class);
    }

    @Override
    public void addChild(ChildDto childDto, Long parentId) {
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", parentId));

        Child child = mapper.map(childDto, Child.class);
        child.setId(null);
        child.setParent(parent);
        childRepository.save(child);
    }

    @Override
    public void updateChild(Long id, ChildDto childDto) {
        Child child = childRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", id));

        User parent = child.getParent();

        child = mapper.map(childDto, Child.class);
        child.setId(id);
        child.setParent(parent);
        childRepository.save(child);
    }

    @Override
    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }
}
