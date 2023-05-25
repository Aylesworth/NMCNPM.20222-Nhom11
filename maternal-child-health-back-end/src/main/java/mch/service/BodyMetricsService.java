package mch.service;

import java.util.List;

import mch.model.BodyMetrics;

public interface BodyMetricsService {
	public List<BodyMetrics> getBodyMetricsOfChild(Long childId);
	
	public void addBodyMetrics(Long childId, BodyMetrics metrics);
	
	public void updateBodyMetrics(Long childId, Long id, BodyMetrics metrics);
	
	public void deleteBodyMetrics(Long childId, Long id);
}
