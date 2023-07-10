package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.config.DateTimeUtils;
import io.github.aylesw.mch.backend.dto.InjectionDto;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
import io.github.aylesw.mch.backend.dto.VaccineStatisticsDetails;
import io.github.aylesw.mch.backend.dto.VaccineSuggestion;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.model.Child;
import io.github.aylesw.mch.backend.model.Injection;
import io.github.aylesw.mch.backend.model.Reaction;
import io.github.aylesw.mch.backend.model.Vaccine;
import io.github.aylesw.mch.backend.repository.*;
import io.github.aylesw.mch.backend.service.GoogleCalendarService;
import io.github.aylesw.mch.backend.service.InjectionService;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InjectionServiceImpl implements InjectionService {

    private final NotificationService notificationService;
    private final GoogleCalendarService googleCalendarService;
    private final InjectionRepository injectionRepository;
    private final ChildRepository childRepository;
    private final UserRepository userRepository;
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

        if (injectionRepository.findByChildIdAndVaccineNameAndDoseNo(
                childId,
                injection.getVaccine().getName(),
                injection.getVaccine().getDoseNo()
        ).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Child already injected with this vaccine");
        }

        saveInjection(injection);

        if (!injection.getStatus().equals("Chờ xác nhận")
                && !injection.getDate().toLocalDate().isBefore(LocalDate.now())) {
            notificationService.createNotificationsAboutInjection(injection);
            googleCalendarService.createEventOfInjection(injection);
        }
        if (injection.getStatus().equals("Chờ xác nhận")) {
            NotificationDetails notificationDetails = NotificationDetails.builder()
                    .title("Đăng ký tiêm chủng mới")
                    .message("Người dùng %s đã đăng ký tiêm vaccine %s mũi %d cho bé %s."
                            .formatted(child.getParent().getFullName(),
                                    injection.getVaccine().getName(),
                                    injection.getVaccine().getDoseNo(),
                                    child.getFullName()))
                    .time(DateTimeUtils.currentTimestamp())
                    .build();

            userRepository.findAdmins().forEach(user -> {
                notificationDetails.setUser(user);
                notificationService.createSystemNotification(notificationDetails);
            });
        }
    }

    @Override
    public void updateInjection(Long childId, Long injectionId, InjectionDto injectionDto) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (!injection.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Injection does not belong to child");

        boolean vaccineChanged = !injectionDto.getVaccineName().equals(injection.getVaccine().getName())
                || !injectionDto.getVaccineDoseNo().equals(injection.getVaccine().getDoseNo());
        boolean injectionExists = injectionRepository.findByChildIdAndVaccineNameAndDoseNo(
                childId,
                injectionDto.getVaccineName(),
                injectionDto.getVaccineDoseNo()
        ).isPresent();

        if (vaccineChanged && injectionExists) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Child already injected with this vaccine");
        }

        Date originalDate = injection.getDate();
        injection = mapToEntity(injectionDto);
        injection.setId(injectionId);
        injection.setChild(child);

        saveInjection(injection);

        if (!injection.getDate().equals(originalDate)
                && !injection.getStatus().equals("Chờ xác nhận")
                && !injection.getDate().toLocalDate().isBefore(LocalDate.now())) {
            notificationService.updateNotificationsAboutInjection(injection);
            googleCalendarService.updateEventOfInjection(injection);
        }
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
        if (!injection.getStatus().equals("Chờ xác nhận")
                && !injection.getDate().toLocalDate().isBefore(LocalDate.now())) {
            notificationService.deleteNotificationsAboutInjection(injection);
            googleCalendarService.deleteEventOfInjection(injection);
        }
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
                .time(DateTimeUtils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }

    @Override
    public List<InjectionDto> getSchedule() {
        return injectionRepository.findAfterDateWithStatusOtherThan(DateTimeUtils.currentDate(), "Chờ xác nhận").stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<InjectionDto> getPendingRegistrations() {
        return injectionRepository.findByStatus("Chờ xác nhận").stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void approveRegistration(Long injectionId) {
        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (injection.getDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Registration already expired");
        }

        injection.setStatus("Đã xác nhận");
        injectionRepository.save(injection);

        notificationService.createNotificationsAboutInjection(injection);
        googleCalendarService.createEventOfInjection(injection);
    }

    @Override
    public void rejectRegistration(Long injectionId, String reason) {
        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        injectionRepository.delete(injection);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(injection.getChild().getParent())
                .time(DateTimeUtils.currentTimestamp())
                .title("Đăng ký tiêm chủng bị hủy")
                .message("Đăng ký tiêm vaccine %s mũi số %d cho bé %s không được phê duyệt với lý do: %s"
                        .formatted(injection.getVaccine().getName(),
                                injection.getVaccine().getDoseNo(),
                                injection.getChild().getFullName(),
                                reason))
                .build();
        notificationService.createSystemNotification(notificationDetails);
    }

    private InjectionDto mapToDto(Injection injection) {
        InjectionDto injectionDto = new InjectionDto();
        injectionDto.setId(injection.getId());
        injectionDto.setChildId(injection.getChild().getId());
        injectionDto.setChildName(injection.getChild().getFullName());
        injectionDto.setChildDob(injection.getChild().getDob());
        injectionDto.setChildAgeInMonths(injection.getChild().getAgeInMonths());
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

    @Override
    public void addReaction(Long childId, Long injectionId, String reaction) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (!injection.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Injection does not belong to child");

        Reaction reactionEntity = Reaction.builder().details(reaction).injection(injection).build();
        reactionRepository.save(reactionEntity);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .time(DateTimeUtils.currentTimestamp())
                .title("Triệu chứng sau tiêm mới")
                .message("Triệu chứng mới sau mũi tiêm %s mũi số %d của bé %s: %s"
                        .formatted(injection.getVaccine().getName(),
                                injection.getVaccine().getDoseNo(),
                                injection.getChild().getFullName(),
                                reaction))
                .build();

        userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .map(role -> role.getName()).toList().contains("ADMIN"))
                .forEach(user -> {
                    notificationDetails.setUser(user);
                    notificationService.createSystemNotification(notificationDetails);
                });
    }

    @Override
    public void removeReaction(Long childId, Long injectionId, String reaction) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        Injection injection = injectionRepository.findById(injectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Injection", "id", injectionId));

        if (!injection.getChild().getId().equals(childId))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Injection does not belong to child");

        reactionRepository.deleteByInjectionIdAndDetails(injectionId, reaction);
    }

    @Override
    public List<VaccineStatisticsDetails> getVaccineStatistics(Integer month, Integer year) {

        List<VaccineStatisticsDetails> vaccineStatistics = new ArrayList<>();
        List<Vaccine> vaccines = vaccineRepository.findAll();

        Date fromDate, toDate;

        if (month == null || year == null) {
            fromDate = Date.valueOf(LocalDate.of(1900, 1, 1));
            toDate = Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        } else {
            fromDate = Date.valueOf(LocalDate.of(year, month, 1));
            toDate = Date.valueOf(fromDate.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()));
        }

        vaccines.forEach(vaccine -> {
            VaccineStatisticsDetails vaccineStatisticsDetails = VaccineStatisticsDetails.builder()
                    .vaccine(vaccine.getName())
                    .doseNo(vaccine.getDoseNo())
                    .quantity(injectionRepository.countByVaccine(vaccine.getName(), vaccine.getDoseNo(), fromDate, toDate))
                    .build();
            vaccineStatistics.add(vaccineStatisticsDetails);
        });

        vaccines.stream()
                .map(vaccine -> vaccine.getName()).collect(Collectors.toSet())
                .forEach(name -> {
                    VaccineStatisticsDetails vaccineStatisticsDetails = VaccineStatisticsDetails.builder()
                            .vaccine(name)
                            .doseNo(0)
                            .quantity(injectionRepository.countByVaccine(name, fromDate, toDate))
                            .build();
                    vaccineStatistics.add(vaccineStatisticsDetails);
                });

        return vaccineStatistics;
    }

    @Override
    public List<VaccineSuggestion> getVaccineSuggestions(Long childId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child", "id", childId));

        return vaccineRepository.findAll().stream()
                .filter(vaccine ->
                        vaccine.getAgeGroup().getMinAgeInDays() <= child.getAgeInDays()
                                && vaccine.getAgeGroup().getMaxAgeInDays() >= child.getAgeInDays())
                .filter(vaccine -> injectionRepository.findByChildIdAndVaccineNameAndDoseNo(
                        childId,
                        vaccine.getName(),
                        vaccine.getDoseNo()
                ).isEmpty())
                .map(vaccine -> VaccineSuggestion.builder()
                        .childId(childId)
                        .childName(child.getFullName())
                        .vaccineName(vaccine.getName())
                        .doseNo(vaccine.getDoseNo())
                        .ageGroupName(vaccine.getAgeGroup().getName())
                        .build()
                ).toList();
    }
}
