/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private String fullName;
    private String citizenId;
    private LocalDate dob;
    private String sex;
    private String phone;
    private String address;
    private String insuranceId;
    private String status;
}
