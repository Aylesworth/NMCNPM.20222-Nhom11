/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.service.impl;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import mch.dao.UserDao;
import mch.dto.UserSignUpDTO;
import mch.exception.InvalidValueException;
import mch.exception.ServiceException;
import mch.model.User;
import mch.security.PasswordEncryptor;
import mch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void signUp(UserSignUpDTO dto) throws ServiceException {
        User user = userDao.findByEmail(dto.getEmail());
        if (user != null)
            throw new ServiceException("Email đã được sử dụng!");
        
        user = userDao.findByPhoneNumber(dto.getPhone());
        if (user != null)
            throw new ServiceException("Số điện thoại đã được sử dụng!");
        
        user = User.builder()
                .email(dto.getEmail())
                .passwordHash(PasswordEncryptor.encrypt(dto.getPassword()))
                .fullName(dto.getFullName())
                .dob(dto.getDob())
                .sex(dto.getSex())
                .phone(dto.getPhone())
                .address(dto.getAddress()).build();
        
        userDao.create(user);
    }

    @Override
    public User signIn(String email, String password) throws ServiceException {
        User user = userDao.findByEmail(email);
        if (user==null)
            throw new ServiceException("Tài khoản không tồn tại");
        
        String passwordHash = PasswordEncryptor.encrypt(password);
        if (!passwordHash.equals(user.getPasswordHash()))
            throw new ServiceException("Mật khẩu không đúng!");
        
        if (user.getStatus().equals("pending"))
            throw new ServiceException("Vui lòng chờ tài khoản được phê duyệt.");
        
        user.setPasswordHash(null);
        return user;
    }
    
}
