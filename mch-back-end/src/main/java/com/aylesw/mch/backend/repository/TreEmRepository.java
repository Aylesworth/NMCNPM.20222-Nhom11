package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.DangKyTreEm;
import com.aylesw.mch.backend.model.ThayDoiTreEm;
import com.aylesw.mch.backend.model.TreEm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreEmRepository extends JpaRepository<TreEm, Long> {

    @Query("SELECT dk FROM DangKyTreEm dk WHERE dk.id = ?1")
    public Optional<DangKyTreEm> findDangKyTreEm(Long dangKyTreEmId);

    @Query("SELECT td FROM ThayDoiTreEm td WHERE td.id = ?1")
    public Optional<ThayDoiTreEm> findThayDoiTreEm(Long thayDoiTreEmId);

    @Query("SELECT t FROM TreEm t WHERE t.tenDayDu LIKE %?1%")
    public List<TreEm> find(String keyword);
}
