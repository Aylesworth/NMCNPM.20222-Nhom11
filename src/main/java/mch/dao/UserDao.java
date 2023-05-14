/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mch.dao;

import mch.model.User;

/**
 *
 * @author Admin
 */
public interface UserDao {
    public void create(User user);
    public User findByEmail(String email);
    public User findByPhoneNumber(String phoneNumber);
}
