package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.InjectionDto;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.model.Child;
import io.github.aylesw.mch.backend.model.Injection;
import io.github.aylesw.mch.backend.model.Reaction;
import io.github.aylesw.mch.backend.model.Vaccine;
import io.github.aylesw.mch.backend.repository.ChildRepository;
import io.github.aylesw.mch.backend.repository.InjectionRepository;
import io.github.aylesw.mch.backend.repository.ReactionRepository;
import io.github.aylesw.mch.backend.repository.VaccineRepository;
import io.github.aylesw.mch.backend.service.InjectionService;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InjectionServiceImpl implements InjectionService {

    private final NotificationService notificationService;
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
        injectionDto.setStatus(injection.getStatus());
        injectionDto.setReactions(injection.getReactions().stream().map(Reaction::getDetails).toList());
        return injectionDto;
    }

    private Injection mapToEntity(InjectionDto injectionDto) {
        Injection injection = new Injection();
        injection.setVaccine(vaccineRepository.findByNameAndDoseNo(
                        injectionDto.getVaccineName(),
                        injectionDto.getVaccineDoseNo()
                ).orElseThrow()
        );
        injection.setDate(injectionDto.getDate());
        injection.setNote(injectionDto.getNote());
        injection.setStatus(injectionDto.getStatus());

        if (injectionDto.getReactions() != null)
            injection.setReactions(
                    injectionDto.getReactions().stream()
                            .map(details -> Reaction.builder().details(details).build())
                            .toList()
            );

        return injection;
    }

    private Injection saveInjection(Injection injection) {
        reactionRepository.deleteAll(reactionRepository.findByInjectionId(injection.getId()));

        List<Reaction> reactions = injection.getReactions();
        injection.setReactions(null);

        final Injection savedInjection = injectionRepository.save(injection);
        if (reactions != null) {
            reactions.forEach(reaction -> reaction.setInjection(savedInjection));
            savedInjection.setReactions(reactionRepository.saveAll(reactions));
        }

        return savedInjection;
    }

    @Override
    public List<Vaccine> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    @Override
    public void handleReaction(Long childId, Long injectionId, String reaction, String advice) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (!injection.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Injection does not belong to child");

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(child.getParent())
                .title("Hướng dẫn xử lý triệu chứng")
                .message("Lời khuyên cho triệu chứng \"%s\" sau mũi tiêm %s số %d của bé %s: %s"
                        .formatted(
                                reaction,
                                injection.getVaccine().getName(),
                                injection.getVaccine().getDoseNo(),
                                child.getFullName(),
                                advice
                        ))
                .time(Utils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }
}
