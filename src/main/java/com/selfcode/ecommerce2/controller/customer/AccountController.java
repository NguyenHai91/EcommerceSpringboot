package com.selfcode.ecommerce2.controller.customer;


import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AccountController {
  @Autowired
  private UserService userService;

  @GetMapping("/account")
  public String account(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    String username = principal.getName();
    User customer = userService.findByUsername(username);
    model.addAttribute("customer", customer);
    return "customer/account";
  }

  @RequestMapping(value="/update-info", method={RequestMethod.PUT, RequestMethod.GET})
  public String updateCustomer(@ModelAttribute("customer") User customer,
                               Model model,
                               RedirectAttributes redirect,
                               Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    User customerSaved = userService.saveInfor(customer);
    redirect.addFlashAttribute("customer", customerSaved);
    return "redirect:/account";
  }
}
