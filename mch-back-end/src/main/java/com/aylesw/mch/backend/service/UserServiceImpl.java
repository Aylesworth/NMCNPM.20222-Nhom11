package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.model.DangKyUser;
import com.aylesw.mch.backend.model.Role;
import com.aylesw.mch.backend.model.ThayDoiUser;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.DangKyUserRepository;
import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.repository.ThayDoiUserRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final DangKyUserRepository dangKyUserRepository;
	private final ThayDoiUserRepository thayDoiUserRepository;

	@Override
	public void approveDangKyUser(Long dangKyUserId) {
		DangKyUser dangKyUser = userRepository.findDangKyUser(dangKyUserId).orElseThrow();
		createUser(dangKyUser);
	}

	@Override
	public void approveThayDoiUser(Long thayDoiUserId) {
		ThayDoiUser thayDoiUser = userRepository.findThayDoiUser(thayDoiUserId).orElseThrow();
		updateUser(thayDoiUser);
	}

	@Override
	public List<User> search(String keyword) {
		return userRepository.find(keyword);
	}

	@Override
	public User getInfo(Long id) {
		return userRepository.findById(id).orElseThrow();
	}

	@Override
	public void createUser(DangKyUser dangKyUser) {
		User user = User.builder()
				.email(dangKyUser.getEmail())
				.password(dangKyUser.getPassword())
				.tenDayDu(dangKyUser.getTenDayDu())
				.gioiTinh(dangKyUser.getGioiTinh())
				.ngaySinh(dangKyUser.getNgaySinh())
				.sdt(dangKyUser.getSdt())
				.diaChi(dangKyUser.getDiaChi())
				.cccd(dangKyUser.getCccd())
				.bhyt(dangKyUser.getBhyt())
				.build();

		Role role = roleRepository.findByName("ROLE_USER").orElseThrow();
		user.setRoles(Set.of(role));
		userRepository.save(user);
	}

	@Override
	public void updateUser(ThayDoiUser thayDoiUser) {
		User user = userRepository.findById(thayDoiUser.getUserId()).orElseThrow();
		user.setTenDayDu(thayDoiUser.getTenDayDu());
		user.setGioiTinh(thayDoiUser.getGioiTinh());
		user.setNgaySinh(thayDoiUser.getNgaySinh());
		user.setSdt(thayDoiUser.getSdt());
		user.setDiaChi(thayDoiUser.getDiaChi());
		user.setCccd(thayDoiUser.getCccd());
		user.setBhyt(thayDoiUser.getBhyt());

		userRepository.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
