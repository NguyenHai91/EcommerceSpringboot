package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.Order;

import javax.servlet.http.HttpSession;
import java.security.Principal;

public interface OrderService {

  Order getById(Long id);

  Order saveOrder(Cart cart, String notes);

  void acceptOrder(Long id, Principal principal, HttpSession session);

  void cancelOrder(Long id);
}
