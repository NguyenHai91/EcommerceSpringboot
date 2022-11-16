package com.selfcode.ecommerce2.controller.admin;


import com.selfcode.ecommerce2.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeAdminController {
  @Autowired
  CategoryServiceImpl categoryService;

  @GetMapping("/admin")
  public String index(Model model, Principal principal) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "redirect:/admin/login";
    }
    model.addAttribute("title", "Categories");
    model.addAttribute("username", principal.getName());
    return "admin/index";
  }

  @GetMapping("/buttons")
  public String buttons(Model model) {
    model.addAttribute("title", "Categories");
    return "buttons";
  }

  @GetMapping("/cards")
  public String cards(Model model) {
    model.addAttribute("title", "Categories");
    return "cards";
  }

}
