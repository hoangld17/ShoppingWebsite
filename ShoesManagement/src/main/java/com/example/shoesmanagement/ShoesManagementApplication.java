package com.example.shoesmanagement;

import com.example.shoesmanagement.model.Consumer;
import com.example.shoesmanagement.model.enums.UserRole;
import com.example.shoesmanagement.model.util.AppUtil;
import com.example.shoesmanagement.service.ConsumerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@SpringBootApplication
public class ShoesManagementApplication implements CommandLineRunner {

    @Autowired
    ConsumerService consumerService;
    @Autowired
    PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(ShoesManagementApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        Consumer consumer = new Consumer();
        consumer.setUsername("Phil Conal");
        String salt = AppUtil.generateSalt();
        consumer.setPasswordSalt(salt);
        consumer.setPasswordHash(encoder.encode("password" + salt));
        consumer.setRole(UserRole.ADMIN);
        consumerService.saveConsumer(consumer);
    }
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
//                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
//                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }
}
