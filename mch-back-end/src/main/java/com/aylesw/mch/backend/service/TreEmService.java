package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.model.DangKyTreEm;
import com.aylesw.mch.backend.model.ThayDoiTreEm;
import com.aylesw.mch.backend.model.TreEm;

import java.util.List;

public interface TreEmService {
    public void approveDangKyTreEm(Long dangKyTreEmId);

    public void approveThayDoiTreEm(Long thayDoiTreEmId);

    public List<TreEm> search(String keyword);

    public TreEm getInfo(Long id);

    public void addTreEm(DangKyTreEm dangKyTreEm);

    public void updateTreEm(ThayDoiTreEm thayDoiTreEm);

    public void deleteTreEm(Long id);
}
