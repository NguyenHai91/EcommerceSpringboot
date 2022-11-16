package com.selfcode.ecommerce2.controller.customer;

import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@Controller
public class AuthController {
  @Autowired
  private UserService userService;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/login")
  public String login(Model model, Principal principal) {
    if (principal != null) {
      return "redirect:/";
    }
    model.addAttribute("userDto", new UserDto());
    return "customer/login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("customerDto", new UserDto());
    return "customer/register";
  }

  @PostMapping("/register")
  public String processRegister(@Valid @ModelAttribute("customerDto") UserDto customerDto,
                                BindingResult result,
                                Model model) {
    try {
      if (result.hasErrors()) {
        model.addAttribute("customerDto", customerDto);
        return "customer/register";
      }
      User customer = userService.findByUsername(customerDto.getUsername());
      if (customer != null) {
        model.addAttribute("username","Username has been registed");
        model.addAttribute("customerDto", customerDto);
        return "customer/register";
      }
      if (customerDto.getPassword().equals(customerDto.getRe_password())) {
        customerDto.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
        userService.save(customerDto);
        model.addAttribute("success", "Register account success");
        return "customer/register";
      } else {
        model.addAttribute("password", "Repeat-password not same password");
        model.addAttribute("customerDto", customerDto);
        return "customer/register";
      }
    } catch (Exception e) {
      model.addAttribute("error", "Server have problem, please try again");
      model.addAttribute("customerDto", customerDto);
    }
    return "customer/register";
  }
}
