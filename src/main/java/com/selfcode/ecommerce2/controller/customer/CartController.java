package com.selfcode.ecommerce2.controller.customer;

import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.*;
import com.selfcode.ecommerce2.service.CartService;
import com.selfcode.ecommerce2.service.UserService;
import com.selfcode.ecommerce2.service.OrderService;
import com.selfcode.ecommerce2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {
  @Autowired
  OrderService orderService;

  @Autowired
  UserService userService;

  @Autowired
  CartService cartService;

  @Autowired
  ProductService productService;

  @GetMapping("/cart")
  public String cart(Model model, Principal principal, HttpSession session) {
    Cart cart = null;
    if (principal == null) {
      if (session.getAttribute("cart") != null) {
        Long cartId = (Long) session.getAttribute("cart");
        cart = cartService.getCartById(cartId).get();
        if (cart != null && cart.getCartItem().size() > 0) {
          Double subTotal = Math.ceil(cart.getTotalPrices());
          model.addAttribute("subTotal", subTotal);
          model.addAttribute("totalItem", cart.getTotalItems());
          model.addAttribute("cart", cart);
          model.addAttribute("username", "");
          return "customer/cart";
        }
      }
      model.addAttribute("check", "No item in cart");
      model.addAttribute("subTotal", 0.0);
      model.addAttribute("totalItem", 0);
      model.addAttribute("username", "");
      return "customer/cart";
    }

    if (principal != null) {
      User customer = userService.findByUsername(principal.getName());
      cart = customer.getCart();
      if (cart != null && cart.getCartItem().size() > 0) {
        model.addAttribute("subTotal", cart.getTotalPrices());
        session.setAttribute("cart", cart.getId());
        model.addAttribute("totalItem", cart.getTotalItems());
        model.addAttribute("username", principal.getName());
        model.addAttribute("cart", cart);
        return "customer/cart";
      }
    } else {
      if (session.getAttribute("cart") != null) {
        Long cartId = (Long) session.getAttribute("cart");
        cart = cartService.getCartById(cartId).get();
        if (cart != null && cart.getCartItem().size() > 0) {
          model.addAttribute("subTotal", cart.getTotalPrices());
          model.addAttribute("totalItem", cart.getTotalItems());
          model.addAttribute("username", principal.getName());
          model.addAttribute("cart", cart);
          return "customer/cart";
        }
      }
    }
    model.addAttribute("check", "No item in cart");
    model.addAttribute("subTotal", 0.0);
    model.addAttribute("totalItem", 0);
    model.addAttribute("username", principal.getName());
    return "customer/cart";
  }

  @RequestMapping(value = "/add-to-cart/{id}", method = {RequestMethod.GET, RequestMethod.POST})
  public String addToCart(@PathVariable("id") Long id,
                          @RequestParam("size") String size,
                          @RequestParam("color") String color,
                          @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                          Principal principal,
                          HttpSession session) {
    Product product = productService.getProductById(id).get();
    Cart cart;
    if (principal == null) {
      if (session.getAttribute("cart") == null) {
        cart = cartService.addItemToCart(product, quantity, null, null);
      } else {
        Long cartId = (Long) session.getAttribute("cart");
        cart = cartService.addItemToCart(product, quantity, null, cartId);
      }
    } else {
      User customer = userService.findByUsername(principal.getName());
      if (session.getAttribute("cart") == null) {
        cart = cartService.addItemToCart(product, quantity, customer, null);
      } else {
        Long cartId = (Long) session.getAttribute("cart");
        cart = cartService.addItemToCart(product, quantity, customer, cartId);
      }
      if (cart.getUser() == null) {
        cart.setUser(customer);
        cartService.save(cart);
      }
    }

    session.setAttribute("cart", cart.getId());
    session.setAttribute("cartItems", cart.getTotalItems());
    return "redirect:/cart";
  }

  @RequestMapping(value ="/update-cart/{id}", params = "action=update", method = {RequestMethod.GET, RequestMethod.POST})
  public String updateCart(@PathVariable("id") Long id,
                           @RequestParam(name = "quantity", defaultValue = "1") Integer quantity,
                           Model model,
                           Principal principal,
                           HttpSession session) {
    if (session.getAttribute("cart") == null && principal == null) {
      return null;
    }

    Product product = productService.getProductById(id).get();
    User customer = null;
    Long cartId = null;
    if (principal != null) {
      customer = userService.findByUsername(principal.getName());
      Cart cart = customer.getCart();
      cartId = cart.getId();
    } else {
      if (session.getAttribute("cart") != null) {
        cartId = (Long) session.getAttribute("cart");
      }
    }
    if (product != null && quantity > 0 && cartId != null) {
      Cart cart = cartService.updateItemInCart(product, quantity, customer, cartId);
      session.setAttribute("cartItems", cart.getTotalItems());
    }
    return "redirect:/cart";
  }

  @RequestMapping(value ="/update-cart/{id}", params = "action=delete", method = {RequestMethod.GET, RequestMethod.POST})
  public String updateCart(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session) {
    if (principal == null && session.getAttribute("cart") == null) {
      return null;
    }
    User customer = null;
    Long cartId = null;
    if (principal != null) {
      customer = userService.findByUsername(principal.getName());
      cartId = (Long)session.getAttribute("cart");
    } else {
      if (session.getAttribute("cart") != null) {
        cartId = (Long)session.getAttribute("cart");
      }
    }
    Product product = productService.getProductById(id).get();
    if (product != null && cartId != null) {
      Cart cart = cartService.deleteItemFromCart(product, customer, cartId);
      session.setAttribute("cartItems", cart.getTotalItems());
    }
    return "redirect:/cart";
  }

  @GetMapping("/checkout")
  public String checkout(Model model, Principal principal, HttpSession session) {
    User customer = null;
    UserDto customerDto = new UserDto();
    Order order = new Order();
    if (principal != null) {
      customer = userService.findByUsername(principal.getName());
      if (customer != null) {
        customerDto = new UserDto(customer);
      }
    }

    if (session.getAttribute("cart") != null) {
      Long cartId = (Long) session.getAttribute("cart");
      Cart cart = cartService.getCartById(cartId).get();
      if (cart != null && cart.getCartItem().size() > 0) {
        model.addAttribute("cart", cart);
        model.addAttribute("customerDto", customerDto);
        model.addAttribute("order", order);
        return "customer/checkout";
      }
    }

    if (customer != null) {
      Cart cart = customer.getCart();
      if (cart != null && cart.getCartItem().size() > 0) {
        model.addAttribute("cart", cart);
        model.addAttribute("customerDto", customerDto);
        model.addAttribute("order", order);
        return "customer/checkout";
      }
    }

    return null;
  }

  @PostMapping("/checkout")
  public String doCheckout(@ModelAttribute("customerDto") UserDto customerDto, Model model, Principal principal, HttpSession session) {
    User customer = null;
    Cart cart = null;
    if (principal != null) {
      customer = userService.findByUsername(principal.getName());
      cart = customer.getCart();

    } else {
      Long cartId = (Long) session.getAttribute("cart");
      cart = cartService.getCartById(cartId).get();
    }

    if (customerDto == null) return null;

    if (cart == null || cart.getCartItem().size() <= 0) return null;

    Order order = new Order();
    for (CartItem item: cart.getCartItem()) {
      OrderDetail orderDetail = new OrderDetail();
    }
    order = orderService.saveOrder(cart);
    if (order != null) {
      return "redirect:/order/" + order.getId();
    }

    return null;
  }

  @GetMapping("/order/{id}")
  public String order(@PathVariable("id") Long id, Model model, Principal principal) {
    User customer = null;
    Cart cart = null;

    if (principal != null) {
      customer = userService.findByUsername(principal.getName());
      cart = customer.getCart();
    }

    Order order = orderService.getById(id);
    if (order == null) {
      return null;
    }
    model.addAttribute("order", order);

//    List<Order> orderList = customer.getOrders();
//    model.addAttribute("customer", customer);
//    model.addAttribute("cart", cart);
//    model.addAttribute("orders", orderList);
    return "customer/order";
  }

  @PostMapping("/order/accept/{id}")
  public String saveOrder(@PathVariable("id") Long id, Model model, Principal principal) {
    try {
      orderService.acceptOrder(id);
      model.addAttribute("success", "Order success");
      return "customer/finish";
    } catch(Exception e) {
      model.addAttribute("error", "Order error");
      return "customer/finish";
    }
  }

  @PostMapping("/order/cancel/{id}")
  public String cancelOrder(@PathVariable("id") Long id, Model model, Principal principal) {
    orderService.cancelOrder(id);
    return "redirect:/cart";
  }

}
