/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.config;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Admin
 */
@Configuration
public class ViewConfig {
    
    @Bean
    public String emailPattern() {
        return new String("[A-Za-z0-9.]+@[A-Za-z0-9.]+");
    }
    
    @Bean
    public String phonePattern() {
        return new String("\\d{9,11}");
    }
    
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    
    @Bean
    public Color errorBackground() {
        return new Color(255, 204, 204);
    }
}
