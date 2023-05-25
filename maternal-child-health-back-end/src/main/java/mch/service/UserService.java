package mch.service;

import java.util.List;

import mch.dto.ChangePasswordRequest;
import mch.dto.UserDto;

public interface UserService {

	public void addRoleToUser(String email, String roleName);
	
	public List<UserDto> getAllUsers();
	
	public UserDto getUserById(Long id);
	
	public void updateUser(UserDto userDto);
	
	public void changePassword(Long id, ChangePasswordRequest request);
}
