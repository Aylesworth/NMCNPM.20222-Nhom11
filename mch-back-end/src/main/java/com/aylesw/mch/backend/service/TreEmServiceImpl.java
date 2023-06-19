package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.model.DangKyTreEm;
import com.aylesw.mch.backend.model.ThayDoiTreEm;
import com.aylesw.mch.backend.model.TreEm;
import com.aylesw.mch.backend.repository.TreEmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreEmServiceImpl implements TreEmService {
    private final TreEmRepository treEmRepository;

    @Override
    public void approveDangKyTreEm(Long dangKyTreEmId) {
        DangKyTreEm dangKyTreEm = treEmRepository.findDangKyTreEm(dangKyTreEmId).orElseThrow();
        addTreEm(dangKyTreEm);
    }

    @Override
    public void approveThayDoiTreEm(Long thayDoiTreEmId) {
        ThayDoiTreEm thayDoiTreEm = treEmRepository.findThayDoiTreEm(thayDoiTreEmId).orElseThrow();
        updateTreEm(thayDoiTreEm);
    }

    @Override
    public List<TreEm> search(String keyword) {
        return treEmRepository.find(keyword);
    }

    @Override
    public TreEm getInfo(Long id) {
        return treEmRepository.findById(id).orElseThrow();
    }

    @Override
    public void addTreEm(DangKyTreEm dangKyTreEm) {
        TreEm treEm = TreEm.builder()
                .tenDayDu(dangKyTreEm.getTenDayDu())
                .gioiTinh(dangKyTreEm.getGioiTinh())
                .ngaySinh(dangKyTreEm.getNgaySinh())
                .danToc(dangKyTreEm.getDanToc())
                .noiSinh(dangKyTreEm.getNoiSinh())
                .bhyt(dangKyTreEm.getBhyt())
                .parentId(dangKyTreEm.getParentId())
                .build();
        treEmRepository.save(treEm);
    }

    @Override
    public void updateTreEm(ThayDoiTreEm thayDoiTreEm) {
        TreEm treEm = treEmRepository.findById(thayDoiTreEm.getTreEmId()).orElseThrow();
        treEm.setTenDayDu(thayDoiTreEm.getTenDayDu());
        treEm.setGioiTinh(thayDoiTreEm.getGioiTinh());
        treEm.setNgaySinh(thayDoiTreEm.getNgaySinh());
        treEm.setDanToc(thayDoiTreEm.getDanToc());
        treEm.setNoiSinh(thayDoiTreEm.getNoiSinh());
        treEm.setBhyt(thayDoiTreEm.getBhyt());
        treEmRepository.save(treEm);
    }

    @Override
    public void deleteTreEm(Long id) {
        treEmRepository.deleteById(id);
    }
}
