/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.dao.impl;

import java.sql.Types;
import mch.dao.UserDao;
import mch.dao.mapper.UserRowMapper;
import mch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRowMapper rowMapper;

    @Override
    public void create(User user) {
        String sql = """
                     INSERT INTO user (email, password_hash, full_name, dob, sex, phone, address, status)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     """;
        jdbcTemplate.update(sql, user.getEmail(), user.getPasswordHash(), user.getFullName(), user.getDob(), user.getSex(), user.getPhone(), user.getAddress(), "pending");
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{email}, new int[]{Types.VARCHAR}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
        return user;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM user WHERE phone = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{phoneNumber}, new int[]{Types.VARCHAR}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
        return user;
    }

}
