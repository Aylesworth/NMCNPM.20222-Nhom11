/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.service;

import mch.dto.UserSignUpDTO;
import mch.exception.ServiceException;
import mch.model.User;

/**
 *
 * @author Admin
 */
public interface UserService {
    public void signUp(UserSignUpDTO dto) throws ServiceException;
    public User signIn(String email, String password) throws ServiceException;
}
