package com.selfcode.ecommerce2.controller.admin;

import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.Role;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.service.RoleService;
import com.selfcode.ecommerce2.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class UserAdminController {
  @Autowired
  UserServiceImpl userService;

  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  RoleService roleService;

  @GetMapping("/admin/login")
  public String loginForm(Model model) {
    model.addAttribute("title", "Login");
    model.addAttribute("userDto", new UserDto());
    return "admin/login";
  }

  @GetMapping("/admin/user/add")
  public String register(Model model) {
    List<Role> roles = roleService.getAll();
    model.addAttribute("title", "Add User");
    model.addAttribute("roles", roles);
    model.addAttribute("userDto", new UserDto());
    return "admin/add_user";
  }

  @PostMapping("/admin/user/add")
  public String createUser(@Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult result,
                           Model model,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
    Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication();
    if (!authorities.contains("ADMIN")) {
      model.addAttribute("userDto", userDto);
      redirectAttributes.addFlashAttribute("error", "You don't have permission");
      return "redirect:/admin/user/add";
    }
    try {
      if (result.hasErrors()) {
        model.addAttribute("userDto", userDto);
        redirectAttributes.addFlashAttribute("error", "Have error in processing create user");
        return "redirect:/admin/user/add";
      }
      String username = userDto.getUsername();
      User user = userService.findByUsername(username);
      if (user != null) {
        model.addAttribute("userDto", userDto);
        redirectAttributes.addFlashAttribute("error", "Your email has been registered");
        return "redirect:/admin/user/add";
      }
      if (!userDto.getPassword().equals(userDto.getRe_password())) {
        redirectAttributes.addFlashAttribute("error", "Re-password not same with password");
        return "redirect:/admin/user/add";
      }
      userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
      userService.saveAdmin(userDto);
      redirectAttributes.addFlashAttribute("success", "Register user success");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Have error, please check values again");
    }
    return "redirect:/admin/user/add";
  }

  @PostMapping("/admin/user/active/{id}")
  public String activeUser(@PathVariable("id") Long id, Principal principal, HttpSession session) {
    User user = userService.getById(id);
    if (user != null) {
      userService.activeUser(id, !user.isActived());
      return "redirect:/admin/users";
    }
    return null;
  }

  @PostMapping("/admin/user/delete/{id}")
  public String deleteUser(@PathVariable("id") Long id, Principal principal, HttpSession session) {
    userService.delete(id);
    return "redirect:/admin/users";
  }

  @GetMapping("/admin/forgot-password")
  public String forgotPassword(Model model) {
    model.addAttribute("title", "Forget password");
    return "/admin/forgot-password";
  }
}
