package mch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mch.model.BodyMetrics;
import mch.model.Child;
import mch.repository.BodyMetricsRepository;
import mch.repository.ChildRepository;

@Service
@RequiredArgsConstructor
public class BodyMetricsServiceImpl implements BodyMetricsService {

	private final BodyMetricsRepository bodyMetricsRepository;
	private final ChildRepository childRepository;

	@Override
	public List<BodyMetrics> getBodyMetricsOfChild(Long childId) {
		return bodyMetricsRepository.findByChild(childId);
	}

	@Override
	public void addBodyMetrics(Long childId, BodyMetrics metrics) {
		metrics.setId(null);
		Child child = childRepository.findById(childId).orElseThrow();
		metrics.setChild(child);
		bodyMetricsRepository.save(metrics);
	}

	@Override
	public void updateBodyMetrics(Long childId, Long id, BodyMetrics metrics) {
		BodyMetrics original = bodyMetricsRepository.findByChildAndId(childId, id).orElseThrow();
		metrics.setId(id);
		metrics.setChild(original.getChild());
		bodyMetricsRepository.save(metrics);
	}

	@Override
	public void deleteBodyMetrics(Long childId, Long id) {
		BodyMetrics metrics = bodyMetricsRepository.findByChildAndId(childId, id).orElseThrow();
		bodyMetricsRepository.delete(metrics);
	}
}
