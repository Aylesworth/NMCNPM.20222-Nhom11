/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.config;

import javax.sql.DataSource;
import mch.App;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Admin
 */
@Configuration
public class DaoConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        DataSource ds = App.getApplicationContext().getBean(DataSource.class);
        return new JdbcTemplate(ds);
    }
}
