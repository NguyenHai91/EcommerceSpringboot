package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.CartItem;
import com.selfcode.ecommerce2.model.Order;
import com.selfcode.ecommerce2.model.OrderDetail;
import com.selfcode.ecommerce2.repository.CartRepository;
import com.selfcode.ecommerce2.repository.OrderDetailRepository;
import com.selfcode.ecommerce2.repository.OrderRepository;
import com.selfcode.ecommerce2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  OrderDetailRepository orderDetailRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  CartRepository cartRepository;

  @Override
  public Order getById(Long id) {
    return  orderRepository.findById(id).get();
  }

  @Override
  public Order saveOrder(Cart cart) {
    Order order = new Order();
    order.setOrderStatus("PENDING");
    order.setOrderDate(new Date());
    order.setTotalPrice(cart.getTotalPrices());
    if (cart.getUser() != null) {
      order.setUser(cart.getUser());
    }
    order = orderRepository.save(order);

    List<OrderDetail> orderDetailList = new ArrayList<>();

    for (CartItem item : cart.getCartItem()) {
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
    cart.setCartItem(new HashSet<>());
    cart.setTotalItems(0);
    cart.setTotalPrices(0);
    cartRepository.save(cart);
    return orderRepository.save(order);
  }

  @Override
  public void acceptOrder(Long id) {
    Order order = orderRepository.getById(id);
    order.setOrderDate(new Date());
    order.setOrderStatus("SHIPPING");
    orderRepository.save(order);
  }

  @Override
  public void cancelOrder(Long id) {
    orderRepository.deleteById(id);
  }
}
