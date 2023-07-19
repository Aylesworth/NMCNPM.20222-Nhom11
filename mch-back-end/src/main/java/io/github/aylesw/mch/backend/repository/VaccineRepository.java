package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    Optional<Vaccine> findByNameAndDoseNo(String name, Integer doseNo);

    @Query("SELECT MAX(v.doseNo) FROM Vaccine v WHERE v.name LIKE ?1")
    int findMaxDoseNo(String name);
}
