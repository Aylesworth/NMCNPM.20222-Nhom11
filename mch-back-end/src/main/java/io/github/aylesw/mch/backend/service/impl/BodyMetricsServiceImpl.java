package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.dto.BodyMetricsDto;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.entity.BodyMetrics;
import io.github.aylesw.mch.backend.entity.Child;
import io.github.aylesw.mch.backend.entity.User;
import io.github.aylesw.mch.backend.repository.BodyMetricsRepository;
import io.github.aylesw.mch.backend.repository.ChildRepository;
import io.github.aylesw.mch.backend.service.BodyMetricsService;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodyMetricsServiceImpl implements BodyMetricsService {
    private final NotificationService notificationService;
    private final BodyMetricsRepository bodyMetricsRepository;
    private final ChildRepository childRepository;
    private final ModelMapper mapper;

    @Override
    public List<BodyMetricsDto> getBodyMetrics(Long childId) {
        return bodyMetricsRepository.findByChildId(childId).stream()
                .map(bodyMetrics -> {
                    BodyMetricsDto dto = mapper.map(bodyMetrics, BodyMetricsDto.class);
                    dto.setChildId(bodyMetrics.getChild().getId());
                    return dto;
                })
                .toList();
    }

    @Override
    public void addBodyMetrics(Long childId, BodyMetricsDto bodyMetricsDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        BodyMetrics bodyMetrics = mapper.map(bodyMetricsDto, BodyMetrics.class);
        bodyMetrics.setId(null);
        bodyMetrics.setChild(child);
        bodyMetricsRepository.save(bodyMetrics);
    }

    @Override
    public void deleteBodyMetrics(Long childId, Long bodyMetricsId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        BodyMetrics bodyMetrics = bodyMetricsRepository.findById(bodyMetricsId)
                .orElseThrow(() -> new ResourceNotFoundException("Body metrics", "id", bodyMetricsId));

        if (!bodyMetrics.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Body metrics do not belong to child");

        bodyMetricsRepository.delete(bodyMetrics);
    }

    @Override
    public void requestUpdate(Long childId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        User user = child.getParent();

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .title("Yêu cầu cập nhật định kỳ")
                .message("Cha/mẹ hãy cập nhật chiều cao, cân nặng định kỳ cho bé " + child.getFullName() + " nhé!")
                .user(user)
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }
}
