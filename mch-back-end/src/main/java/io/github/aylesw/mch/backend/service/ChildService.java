package io.github.aylesw.mch.backend.service;

import io.github.aylesw.mch.backend.dto.ChildDto;
import io.github.aylesw.mch.backend.model.ChildChange;
import io.github.aylesw.mch.backend.model.ChildRegistration;

import java.util.List;

public interface ChildService {

    List<ChildDto> getByParentId(Long parentId);

    void registerChild(ChildDto childDto, Long parentId);

    void requestChildChange(Long childId, ChildDto childDto);

    public List<ChildRegistration> getAllPendingRegistrations();

    public List<ChildChange> getAllPendingChanges();

    void approveChildRegistration(Long childRegistrationId);

    void approveChildChange(Long childChangeId);

    List<ChildDto> search(String keyword);

    ChildDto getChild(Long childId);

    void addChild(ChildDto childDto, Long parentId);

    void updateChild(Long id, ChildDto childDto);

    void deleteChild(Long id);
}
