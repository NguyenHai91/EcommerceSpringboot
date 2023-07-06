package com.selfcode.ecommerce2.controller.admin;


import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.service.UserService;
import com.selfcode.ecommerce2.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeAdminController {

  @Autowired
  UserService userService;

  @Autowired
  CategoryServiceImpl categoryService;

  @GetMapping("/admin/dashboard")
  public String dashboard(Model model, Principal principal, HttpSession session) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "redirect:/admin/login";
    }
    model.addAttribute("title", "Home");
    model.addAttribute("username", authentication.getName());
    return "admin/index";
  }

  @RequestMapping(value = {"/admin", "/admin/users"}, method = {RequestMethod.GET})
  public String admin(Model model, Principal principal, HttpSession session) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "redirect:/admin/login";
    }
    List<User> users = userService.getAll();
    model.addAttribute("title", "Home");
    model.addAttribute("username", authentication.getName());
    model.addAttribute("users", users);
    return "admin/users";
  }

}
