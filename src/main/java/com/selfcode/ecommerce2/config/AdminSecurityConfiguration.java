package com.selfcode.ecommerce2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(1)
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public UserDetailsService adminDetailsService() {
    return new MyUserServiceConfig();
  }

  @Bean
  @Primary
  public BCryptPasswordEncoder passwordEncoder1() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider1() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(adminDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder1());
    return authProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authenticationProvider(authenticationProvider1());
    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.authorizeRequests().antMatchers("/").permitAll();
    http.authorizeRequests().antMatchers("/h2/**").permitAll();
    http.antMatcher("/admin/**")
        .authorizeRequests().anyRequest().permitAll()
        .and()
          .formLogin()
          .loginPage("/admin/login")
          .loginProcessingUrl("/admin/login")
          .defaultSuccessUrl("/admin")
          .permitAll()
        .and()
          .logout()
          .logoutUrl("/admin/logout")
          .logoutSuccessUrl("/admin");
  }

}
