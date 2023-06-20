package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.ExaminationDto;
import com.aylesw.mch.backend.exception.ApiException;
import com.aylesw.mch.backend.exception.ResourceNotFoundException;
import com.aylesw.mch.backend.model.Child;
import com.aylesw.mch.backend.model.Examination;
import com.aylesw.mch.backend.repository.ChildRepository;
import com.aylesw.mch.backend.repository.ExaminationRepository;
import com.aylesw.mch.backend.repository.MedicineRepository;
import com.aylesw.mch.backend.service.ExaminationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {
    private final ExaminationRepository examinationRepository;
    private final MedicineRepository medicineRepository;
    private final ChildRepository childRepository;
    private final ModelMapper mapper;

    @Override
    public List<ExaminationDto> getExaminations(Long childId) {
        return examinationRepository.findByChildId(childId).stream()
                .map(examination -> mapper.map(examination, ExaminationDto.class))
                .toList();
    }

    @Override
    public void addExamination(Long childId, ExaminationDto examinationDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        examinationDto.setMedicines(
                examinationDto.getMedicines().stream()
                        .map(medicine -> {
                            if (medicineRepository.existsByName(medicine.getName()))
                                return medicineRepository.findByName(medicine.getName());
                            return medicineRepository.save(medicine);
                        }).toList()
        );

        Examination examination = mapper.map(examinationDto, Examination.class);
        examination.setId(null);
        examination.setChild(child);
        examinationRepository.save(examination);
    }

    @Override
    public void updateExamination(Long childId, Long examinationId, ExaminationDto examinationDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Examination examination = examinationRepository.findById(examinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Examination", "id", examinationId));

        if (!examination.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Examination does not belong to child");

        examinationDto.setMedicines(
                examinationDto.getMedicines().stream()
                        .map(medicine -> {
                            if (medicineRepository.existsByName(medicine.getName()))
                                return medicineRepository.findByName(medicine.getName());
                            return medicineRepository.save(medicine);
                        }).toList()
        );

        examination = mapper.map(examinationDto, Examination.class);
        examination.setId(examinationId);
        examination.setChild(child);
        examinationRepository.save(examination);
    }

    @Override
    public void delete(Long childId, Long examinationId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Examination examination = examinationRepository.findById(examinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Examination", "id", examinationId));

        if (!examination.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Examination does not belong to child");

        examinationRepository.delete(examination);
    }
}
