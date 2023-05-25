package mch.service;

import java.util.List;

import mch.dto.ChildDto;

public interface ChildService {
	public List<ChildDto> getAllChildren();
	
	public List<ChildDto> getChildrenOfUser(Long userId);
	
	public ChildDto getChildById(Long id);
	
	public void addChild(ChildDto childDto);
	
	public void updateChild(ChildDto childDto);
}
