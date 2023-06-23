package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.dto.InjectionDto;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.model.Child;
import io.github.aylesw.mch.backend.model.Injection;
import io.github.aylesw.mch.backend.model.Reaction;
import io.github.aylesw.mch.backend.repository.ChildRepository;
import io.github.aylesw.mch.backend.repository.InjectionRepository;
import io.github.aylesw.mch.backend.repository.ReactionRepository;
import io.github.aylesw.mch.backend.repository.VaccineRepository;
import io.github.aylesw.mch.backend.service.InjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InjectionServiceImpl implements InjectionService {

    private final InjectionRepository injectionRepository;
    private final ChildRepository childRepository;
    private final VaccineRepository vaccineRepository;
    private final ReactionRepository reactionRepository;

    @Override
    public List<InjectionDto> getInjections(Long childId) {
        return injectionRepository.findByChildId(childId).stream()
                .map(this::mapToDto).toList();
    }

    @Override
    public void addInjection(Long childId, InjectionDto injectionDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = mapToEntity(injectionDto);
        injection.setChild(child);
        injection.setConfirmed(true);
        injectionRepository.save(injection);
        saveInjection(injection);
    }

    @Override
    public void updateInjection(Long childId, Long injectionId, InjectionDto injectionDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (!injection.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Injection does not belong to child");

        injection = mapToEntity(injectionDto);
        injection.setId(injectionId);
        injection.setChild(child);
        injection.setConfirmed(true);
        saveInjection(injection);
    }

    @Override
    public void deleteInjection(Long childId, Long injectionId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (!injection.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Injection does not belong to child");

        injectionRepository.delete(injection);
    }

    private InjectionDto mapToDto(Injection injection) {
        InjectionDto injectionDto = new InjectionDto();
        injectionDto.setId(injection.getId());
        injectionDto.setChildId(injection.getChild().getId());
        injectionDto.setChildName(injection.getChild().getFullName());
        injectionDto.setVaccineId(injection.getVaccine().getId());
        injectionDto.setVaccineName(injection.getVaccine().getName());
        injectionDto.setVaccineDoseNo(injection.getVaccine().getDoseNo());
        injectionDto.setDate(injection.getDate());
        injectionDto.setNote(injection.getNote());
        injectionDto.setReactions(injection.getReactions().stream().map(Reaction::getDetails).toList());
        return injectionDto;
    }

    private Injection mapToEntity(InjectionDto injectionDto) {
        Injection injection = new Injection();
        injection.setVaccine(vaccineRepository.findById(injectionDto.getVaccineId())
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", injectionDto.getVaccineId())));
        injection.setDate(injectionDto.getDate());
        injection.setNote(injectionDto.getNote());

        injection.setReactions(
                injectionDto.getReactions().stream()
                        .map(details -> Reaction.builder().details(details).build())
                        .toList()
        );

        injection.setConfirmed(false);
        return injection;
    }

    private Injection saveInjection(Injection injection) {
        reactionRepository.deleteAll(reactionRepository.findByInjectionId(injection.getId()));

        List<Reaction> reactions = injection.getReactions();
        injection.setReactions(null);

        final Injection savedInjection = injectionRepository.save(injection);
        reactions.forEach(reaction -> reaction.setInjection(savedInjection));
        savedInjection.setReactions(reactionRepository.saveAll(reactions));

        return savedInjection;
    }
}
