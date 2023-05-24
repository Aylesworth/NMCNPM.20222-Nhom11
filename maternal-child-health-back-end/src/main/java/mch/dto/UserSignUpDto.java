/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Admin
 * 
 */
@Data
@Builder
public class UserSignUpDto {
    private String email;
    private String password;
    private String fullName;
    private LocalDate dob;
    private String sex;
    private String phone;
    private String address;
}
