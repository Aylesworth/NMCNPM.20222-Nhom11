package com.aylesw.mch.backend.service;


import com.aylesw.mch.backend.model.DangKyUser;
import com.aylesw.mch.backend.model.ThayDoiUser;
import com.aylesw.mch.backend.model.User;

import java.util.List;

public interface UserService {

	public void approveDangKyUser(Long dangKyUserId);

	public void approveThayDoiUser(Long thayDoiUserId);

	public List<User> search(String keyword);

	public User getInfo(Long id);

	public void createUser(DangKyUser dangKyUser);

	public void updateUser(ThayDoiUser thayDoiUser);

	public void deleteUser(Long id);
}
