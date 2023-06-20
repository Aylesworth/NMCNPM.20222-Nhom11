package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.ChildDto;

import java.util.List;

public interface ChildService {
    void approveChildRegistration(Long childRegistrationId);

    void approveChildChange(Long childChangeId);

    List<ChildDto> search(String keyword);

    ChildDto getChild(Long childId);

    void addChild(ChildDto childDto, Long parentId);

    void updateChild(Long id, ChildDto childDto);

    void deleteChild(Long id);
}
