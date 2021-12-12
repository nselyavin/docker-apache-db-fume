package ru.meetsapp.Meets.App.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncode(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncode());
    }
}
