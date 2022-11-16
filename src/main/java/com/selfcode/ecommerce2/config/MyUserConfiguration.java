package com.selfcode.ecommerce2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class MyUserConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  protected UserDetailsService userDetailsService() {
    return new MyUserServiceConfig();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/*").permitAll()
        .antMatchers("/admin/**").hasAuthority("ADMIN")
        .antMatchers("/customer/**").hasAuthority("CUSTOMER")
        .and()
        .formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/").permitAll()
        .and()
        .logout().invalidateHttpSession(true).clearAuthentication(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/404");

  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/admin/**")
    .and().ignoring().antMatchers("/customer/**");
  }

//  @Configuration
//  @Order(1)
//  public static class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter{
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//      http.csrf().disable()
//          .authorizeRequests()
//          .antMatchers("/*").permitAll()
//          .antMatchers("/admin").hasAuthority("ADMIN")
//          .and()
//          .formLogin().loginPage("/admin/login").loginProcessingUrl("/admin-login").defaultSuccessUrl("/admin").permitAll()
//          .and()
//          .logout().invalidateHttpSession(true).clearAuthentication(true)
//          .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout")).logoutSuccessUrl("/admin/login?logout").permitAll()
//          .and()
//          .exceptionHandling().accessDeniedPage("/404");
//    }
//  }
//
//  @Configuration
//  @Order(2)
//  public static class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//      http.csrf().disable()
//          .authorizeRequests()
//          .antMatchers("/*").permitAll()
//          .antMatchers("/customer").hasAuthority("CUSTOMER")
//          .and()
//          .formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/").permitAll()
//          .and()
//          .logout().invalidateHttpSession(true).clearAuthentication(true)
//          .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll()
//          .and()
//          .exceptionHandling().accessDeniedPage("/404");
//
//    }
//  }

}
