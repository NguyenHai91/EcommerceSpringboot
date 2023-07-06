package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.*;
import com.selfcode.ecommerce2.repository.CartRepository;
import com.selfcode.ecommerce2.repository.OrderDetailRepository;
import com.selfcode.ecommerce2.repository.OrderRepository;
import com.selfcode.ecommerce2.service.CartService;
import com.selfcode.ecommerce2.service.OrderService;
import com.selfcode.ecommerce2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  OrderDetailRepository orderDetailRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  CartRepository cartRepository;

  @Autowired
  UserService userService;

  @Autowired
  CartService cartService;

  @Override
  public Order getById(Long id) {
    if (orderRepository.findById(id).isPresent()) {
      return orderRepository.findById(id).get();
    }
    return null;
  }

  @Override
  public Order saveOrder(Cart cart, String notes) {
    Order order = new Order();
    order.setNotes(notes);
    order.setOrderStatus("PENDING");
    order.setOrderDate(new Date());
    order.setTotalPrice(cart.getTotalPrices());
    if (cart.getUser() != null) {
      order.setUser(cart.getUser());
    }
    order = orderRepository.save(order);

    List<OrderDetail> orderDetailList = new ArrayList<>();

    for (CartItem item : cart.getCartItems()) {
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setOrder(order);
      orderDetail.setQuantity(item.getQuantity());
      orderDetail.setProduct(item.getProduct());
      orderDetail.setUnitPrice(item.getProduct().getCostPrice());
      orderDetail.setTotalPrice(item.getTotalPrice());
      orderDetailRepository.save(orderDetail);
      orderDetailList.add(orderDetail);
    }
    order.setOrderDetailList(orderDetailList);
    cart.setCartItems(new HashSet<>());
    cart.setTotalItems(0);
    cart.setTotalPrices(0);
    cartRepository.save(cart);
    return orderRepository.save(order);
  }

  @Override
  public void acceptOrder(Long id, Principal principal, HttpSession session) {
    Order order = orderRepository.getById(id);
    order.setOrderDate(new Date());
    order.setOrderStatus("SHIPPING");
    orderRepository.save(order);
    Cart cart = cartService.getCartExisting(principal, session);
    User customer = null;

    if (principal != null && principal.getName() != null) {
      customer = userService.findByUsername(principal.getName());
    }
    cartService.clearCart(cart.getId(), customer);
  }

  @Override
  public void cancelOrder(Long id) {
    orderRepository.deleteById(id);
  }

}
