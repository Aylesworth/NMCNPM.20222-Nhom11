package io.github.aylesw.mch.backend.service;

import io.github.aylesw.mch.backend.dto.ExaminationDto;

import java.util.List;

public interface ExaminationService {
    List<ExaminationDto> getExaminations(Long childId);

    void addExamination(Long childId, ExaminationDto examinationDto);

    void updateExamination(Long childId, Long examinationId, ExaminationDto examinationDto);

    void delete(Long childId, Long examinationId);
}
