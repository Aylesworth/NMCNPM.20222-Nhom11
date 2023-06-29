package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    Optional<Vaccine> findByNameAndDoseNo(String name, Integer doseNo);
}
