package com.nguyenkhanh.backend.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nguyenkhanh.backend.entity.ERole;
import com.nguyenkhanh.backend.exception.CustomAccessDeniedHandler;
import com.nguyenkhanh.backend.jwt.AuthEntryPointJwt;
import com.nguyenkhanh.backend.jwt.AuthTokenFilter;
import com.nguyenkhanh.backend.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthEntryPointJwt authenticationEntryPoint;

    @Bean
    public AuthTokenFilter authenticationTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()//Authen-xác thực
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()//Forbidden-kiểm tra truy cập
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/upload").permitAll()
                .antMatchers("/files").permitAll()
                .antMatchers("/files/**").permitAll()
                .antMatchers("/api/search*").permitAll()
                .antMatchers("/api/test/all").permitAll()
                .antMatchers("/api/v1/**").permitAll()
                .antMatchers("/api/**").hasAuthority(ERole.ROLE_ADMIN.toString().toUpperCase())
                .anyRequest().authenticated();

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
