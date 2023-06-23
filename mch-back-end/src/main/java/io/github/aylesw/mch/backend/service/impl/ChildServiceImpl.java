package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.ChildDto;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.model.Child;
import io.github.aylesw.mch.backend.model.ChildChange;
import io.github.aylesw.mch.backend.model.ChildRegistration;
import io.github.aylesw.mch.backend.model.User;
import io.github.aylesw.mch.backend.repository.ChildChangeRepository;
import io.github.aylesw.mch.backend.repository.ChildRegistrationRepository;
import io.github.aylesw.mch.backend.repository.ChildRepository;
import io.github.aylesw.mch.backend.repository.UserRepository;
import io.github.aylesw.mch.backend.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
    public List<ChildDto> getByParentId(Long parentId) {
        return childRepository.findByParentId(parentId).stream()
                .map(child -> mapper.map(child, ChildDto.class))
                .toList();
    }

    @Override
    public void registerChild(ChildDto childDto, Long parentId) {
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", parentId));

        ChildRegistration childRegistration = mapper.map(childDto, ChildRegistration.class);
        childRegistration.setParent(parent);
        childRegistration.setRequested(Utils.currentTimestamp());

        childRegistrationRepository.save(childRegistration);
    }

    @Override
    public void requestChildChange(Long childId, ChildDto childDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        ChildChange childChange = mapper.map(childDto, ChildChange.class);

        childChange.setChild(child);
        childChange.setRequested(Utils.currentTimestamp());
        childChangeRepository.save(childChange);
    }

    @Override
    public List<ChildRegistration> getAllPendingRegistrations() {
        return childRegistrationRepository.findNotApproved();
    }

    @Override
    public List<ChildChange> getAllPendingChanges() {
        return childChangeRepository.findNotApproved();
    }

    @Override
    public void approveChildRegistration(Long childRegistrationId) {
        ChildRegistration childRegistration = childRegistrationRepository.findById(childRegistrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Child registration", "id", childRegistrationId));

        if (childRegistration.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Child registration already approved");

        Child child = mapper.map(childRegistration, Child.class);
        child.setId(null);

        childRepository.save(child);

        childRegistration.setApproved(Utils.currentTimestamp());
        childRegistrationRepository.save(childRegistration);
    }

    @Override
    public void approveChildChange(Long childChangeId) {
        ChildChange childChange = childChangeRepository.findById(childChangeId)
                .orElseThrow(() -> new ResourceNotFoundException("Child change", "id", childChangeId));

        if (childChange.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Child profile change already approved");

        Child child = mapper.map(childChange, Child.class);
        child.setId(childChange.getChild().getId());
        child.setParent(childChange.getChild().getParent());

        childRepository.save(child);

        childChange.setApproved(Utils.currentTimestamp());
        childChangeRepository.save(childChange);
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
