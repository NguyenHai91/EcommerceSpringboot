package com.selfcode.ecommerce2.controller.admin;

import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserAdminController {
  @Autowired
  UserServiceImpl userService;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/admin/login")
  public String loginForm(Model model) {
    model.addAttribute("title", "Login");
    model.addAttribute("userDto", new UserDto());
    return "admin/login";
  }

  @GetMapping("/admin/register")
  public String registerForm(Model model) {
    model.addAttribute("title", "Register");
    model.addAttribute("userDto", new UserDto());
    return "admin/register";
  }

  @PostMapping("/admin/register")
  public String createUser(@Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
    try {
      if (result.hasErrors()) {
        model.addAttribute("userDto", userDto);
        redirectAttributes.addFlashAttribute("error", "Have error in processing create user");
        return "redirect:/admin/register";
      }
      String username = userDto.getUsername();
      User user = userService.findByUsername(username);
      if (user != null) {
        model.addAttribute("userDto", userDto);
        redirectAttributes.addFlashAttribute("error", "Your email has been registered");
        return "redirect:/admin/register";
      }
      if (!userDto.getPassword().equals(userDto.getRe_password())) {
        redirectAttributes.addFlashAttribute("error", "Re-password not same with password");
        return "redirect:/admin/register";
      }
      userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
      userService.saveAdmin(userDto);
      redirectAttributes.addFlashAttribute("success", "Register user success");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Have error, please check values again");
    }
    return "redirect:/admin/register";
  }

  @GetMapping("/admin/forgot-password")
  public String forgotPassword(Model model) {
    model.addAttribute("title", "Forget password");
    return "/admin/forgot-password";
  }
}
