package mch.service;

import java.util.List;

import mch.model.Examination;

public interface ExaminationService {
	public List<Examination> getExaminationsOfChild(Long childId);

	public void addExamination(Long childId, Examination examination);

	public void updateExamination(Long childId, Long id, Examination examination);

	public void deleteExamination(Long childId, Long id);
}
