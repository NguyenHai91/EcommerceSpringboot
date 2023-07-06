package com.selfcode.ecommerce2.controller.customer;

import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.service.CategoryService;
import com.selfcode.ecommerce2.service.UserService;
import com.selfcode.ecommerce2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class HomeController {
  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ProductService productService;

  @Autowired
  private UserService userService;

  @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
  public String index(Model model, Principal principal, HttpSession session) {
    if (principal != null) {
      session.setAttribute("username", principal.getName());
      User user = userService.findByUsername(principal.getName());
      if (user.getCart() != null) {
        Cart cart = user.getCart();
        session.setAttribute("totalItems", cart.getTotalItems());
      }
      session.setAttribute("totalItems", 0);
    } else {
      session.removeAttribute("username");
    }
    List<Category> categories = categoryService.getMainCategories();
    List<ProductDto> products = productService.getProductsWithMainCategories();
    model.addAttribute("categories", categories);
    model.addAttribute("products", products);
    return "customer/index";
  }

  @GetMapping("/home")
  public String home(Model model, Principal principal, HttpSession session) {
    if (principal != null) {
      session.setAttribute("username", principal.getName());
      User user = userService.findByUsername(principal.getName());
      if (user.getCart() != null) {
        Cart cart = user.getCart();
        session.setAttribute("totalItems", cart.getTotalItems());
      }
      session.setAttribute("totalItems", 0);
    } else {
      session.removeAttribute("username");
    }
    return "customer/home";
  }

  @GetMapping("/blog")
  public String blog(Model model) {
    return "customer/blog";
  }

  @GetMapping("/contact")
  public String contact(Model model) {
    return "customer/contact";
  }

  @GetMapping("/about")
  public String about(Model model) {
    return "customer/about";
  }

  @GetMapping("/404")
  public String pageNotFound() {
    return "admin/404";
  }
}
