package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.Injection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InjectionRepository extends JpaRepository<Injection, Long> {
    @Query("SELECT i FROM Injection i JOIN FETCH i.child c WHERE c.id = ?1 ORDER BY i.date")
    List<Injection> findByChildId(Long childId);

    @Query("SELECT i FROM Injection i WHERE i.date >= ?1 AND i.status NOT LIKE ?2 ORDER BY i.date")
    List<Injection> findAfterDateWithStatusOtherThan(Date date, String status);

    @Query("SELECT i FROM Injection i WHERE i.status LIKE ?1 ORDER BY i.date")
    List<Injection> findByStatus(String status);

    @Query("SELECT i FROM Injection i JOIN FETCH i.child c JOIN FETCH i.vaccine v " +
            "WHERE c.id = ?1 AND v.name LIKE ?2 AND v.doseNo = ?3")
    Optional<Injection> findByChildIdAndVaccineNameAndDoseNo(Long childId, String vaccineName, Integer doseNo);

    @Query("SELECT COUNT(i) FROM Injection i JOIN i.vaccine v " +
            "WHERE v.name LIKE ?1 AND i.date >= ?2 AND i.date <= ?3")
    long countByVaccine(String vaccine, Date fromDate, Date toDate);

    @Query("SELECT COUNT(i) FROM Injection i JOIN i.vaccine v " +
            "WHERE v.name LIKE ?1 AND v.doseNo = ?2 AND i.date >= ?3 AND i.date <= ?4")
    long countByVaccine(String vaccine, Integer doseNo, Date fromDate, Date toDate);

}
