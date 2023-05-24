/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.controller;

import mch.dto.UserSignUpDto;
import mch.exception.ServiceException;
import mch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    public void signIn(String email, String password) throws ServiceException {
        userService.signIn(email, password);
    }
    
    public void signUp(UserSignUpDto dto) throws ServiceException {
        userService.signUp(dto);
    }
}
