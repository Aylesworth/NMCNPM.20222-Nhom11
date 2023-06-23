package io.github.aylesw.mch.backend.service;

import io.github.aylesw.mch.backend.dto.InjectionDto;

import java.util.List;

public interface InjectionService {
    List<InjectionDto> getInjections(Long childId);

    void addInjection(Long childId, InjectionDto injectionDto);

    void updateInjection(Long childId, Long injectionId, InjectionDto injectionDto);

    void deleteInjection(Long childId, Long injectionId);
}
