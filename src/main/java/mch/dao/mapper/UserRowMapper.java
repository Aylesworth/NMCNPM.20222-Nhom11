/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import mch.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setCitizenId(rs.getString("citizen_id"));
        user.setDob(rs.getDate("dob").toLocalDate());
        user.setSex(rs.getString("sex"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setInsuranceId(rs.getString("insurance_id"));
        user.setStatus(rs.getString("status"));
        return user;
    }
    
}
