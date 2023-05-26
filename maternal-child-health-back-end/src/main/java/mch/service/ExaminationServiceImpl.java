package mch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mch.model.Child;
import mch.model.Examination;
import mch.repository.ChildRepository;
import mch.repository.ExaminationRepository;

@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

	private final ExaminationRepository examinationRepository;
	private final ChildRepository childRepository;

	@Override
	public List<Examination> getExaminationsOfChild(Long childId) {
		return examinationRepository.findByChild(childId);
	}

	@Override
	public void addExamination(Long childId, Examination examination) {
		Child child = childRepository.findById(childId).orElseThrow();
		examination.setId(null);
		examination.setChild(child);
		examinationRepository.save(examination);
	}

	@Override
	public void updateExamination(Long childId, Long id, Examination examination) {
		Examination original = examinationRepository.findByChildAndId(childId, id).orElseThrow();
		examination.setId(id);
		examination.setChild(original.getChild());
		examinationRepository.save(examination);
	}

	@Override
	public void deleteExamination(Long childId, Long id) {
		Examination examination = examinationRepository.findByChildAndId(childId, id).orElseThrow();
		examinationRepository.delete(examination);
	}

}
