package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.Order;

public interface OrderService {

  Order getById(Long id);

  Order saveOrder(Cart cart);

  void acceptOrder(Long id);

  void cancelOrder(Long id);
}
