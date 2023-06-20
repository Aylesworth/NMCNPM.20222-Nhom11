package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.BodyMetricsDto;

import java.util.List;

public interface BodyMetricsService {
    List<BodyMetricsDto> getBodyMetrics(Long childId);

    void addBodyMetrics(Long childId, BodyMetricsDto bodyMetricsDto);

    void deleteBodyMetrics(Long childId, Long bodyMetricsId);

    void requestUpdate(Long childId);

}
