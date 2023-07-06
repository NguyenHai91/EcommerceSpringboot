package com.selfcode.ecommerce2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(2)
public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public UserDetailsService userDetailsService() {
    return new MyUserServiceConfig();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder2() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider2() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder2());
    return authProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authenticationProvider(authenticationProvider2());
    http.csrf().disable()
        .authorizeRequests().antMatchers("/customer/**").authenticated()
        .antMatchers("/*").permitAll()
        .and()
          .formLogin()
          .loginPage("/login")
          .loginProcessingUrl("/login")
          .defaultSuccessUrl("/")
          .permitAll()
        .and()
          .logout()
          .logoutUrl("/logout")
          .logoutSuccessUrl("/");
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/resources/**", "/customer/fonts/**", "/customer/css/**", "/customer/js/**", "/customer/images/**", "/customer/vendor/**");
    web.ignoring().antMatchers("/admin/dist/**", "/admin/data/**", "/admin/pages/**", "/admin/css/**", "/admin/js/**", "/admin/img/**", "/admin/vendor/**");
  }
}
