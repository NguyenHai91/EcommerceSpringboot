package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.model.User;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

public interface CartService {
  Cart save(Cart cart);

  Optional<Cart> getCartById(Long id);

  Cart getCartExisting(Principal principal, HttpSession session);

  Cart addItemToCart(Product product, int quantity, User user, Long cartId);

  Cart updateItemInCart(Product product, int quantity, User user, Long cartId);

  Cart deleteItemFromCart(Product product, User user, Long cartId);

  void clearCart(Long idCart, User user);

  void deleteCart(Cart cart);
}
