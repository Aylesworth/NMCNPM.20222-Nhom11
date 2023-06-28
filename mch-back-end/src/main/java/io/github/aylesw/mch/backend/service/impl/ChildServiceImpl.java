package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.ChildDto;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
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
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private final NotificationService notificationService;
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

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(childRegistration.getParent())
                .title("Hồ sơ trẻ được phê duyệt")
                .message("Hồ sơ của bé %s mà bạn đăng ký đã được phê duyệt thành công."
                        .formatted(childRegistration.getFullName()))
                .time(Utils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }

    @Override
    public void rejectChildRegistration(Long childRegistrationId, String reason) {
        ChildRegistration childRegistration = childRegistrationRepository.findById(childRegistrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Child registration", "id", childRegistrationId));

        if (childRegistration.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Child registration already approved");

        childRegistrationRepository.delete(childRegistration);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(childRegistration.getParent())
                .title("Hồ sơ trẻ bị từ chối")
                .message("Hồ sơ của bé %s mà bạn đăng ký đã bị từ chối với lý do: %s"
                        .formatted(childRegistration.getFullName(), reason))
                .time(Utils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
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

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(childChange.getChild().getParent())
                .title("Thay đổi hồ sơ trẻ được phê duyệt")
                .message("Những thay đổi với hồ sơ bé %s đã được phê duyệt thành công."
                        .formatted(childChange.getFullName()))
                .time(Utils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }

    @Override
    public void rejectChildChange(Long childChangeId, String reason) {
        ChildChange childChange = childChangeRepository.findById(childChangeId)
                .orElseThrow(() -> new ResourceNotFoundException("Child change", "id", childChangeId));

        if (childChange.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Child profile change already approved");

        childChangeRepository.delete(childChange);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(childChange.getChild().getParent())
                .title("Thay đổi hồ sơ trẻ bị từ chối")
                .message("Những thay đổi với hồ sơ bé %s đã bị từ chối với lý do: %s."
                        .formatted(childChange.getFullName(), reason))
                .time(Utils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }

    @Override
    public List<ChildDto> search(String keyword) {
        return childRepository.findByKeyword(keyword).stream()
                .map(child -> mapper.map(child, ChildDto.class))
                .toList();
    }

    @Override
    public List<ChildDto> getAllChildren() {
        return childRepository.findAll().stream()
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
