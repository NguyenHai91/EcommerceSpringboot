package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.CartItem;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.repository.CartItemRepository;
import com.selfcode.ecommerce2.repository.CartRepository;
import com.selfcode.ecommerce2.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class CartServiceImpl implements CartService {
  @Autowired
  CartRepository cartRepository;

  @Autowired
  CartItemRepository cartItemRepository;


  @Override
  public Cart save(Cart cart) {
    return cartRepository.save(cart);
  }

  @Override
  public Optional<Cart> getCartById(Long id) {
    return cartRepository.findById(id);
  }

  @Override
  public Cart addItemToCart(Product product, int quantity, User user, Long cartId) {
    Cart cart = null;
    if (cartId != null) {
      cart = cartRepository.findById(cartId).get();
    } else {
      if(user != null) {
        cart = user.getCart();
      }
    }

    if (cart == null) {
      cart = new Cart();
      cart = cartRepository.save(cart);
    }
    Set<CartItem> cartItems = cart.getCartItem();
    CartItem cartItem = findCartItem(cartItems, product.getId());
    if (cartItems == null) {
      cartItems = new HashSet<>();
      if (cartItem == null) {
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setTotalPrice(quantity * product.getCostPrice());
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);
        cartItem = cartItemRepository.save(cartItem);
        cartItems.add(cartItem);
      }
    } else {
      if (cartItem == null) {
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setTotalPrice(quantity * product.getCostPrice());
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);
        cartItem = cartItemRepository.save(cartItem);
        cartItems.add(cartItem);
      } else {
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setTotalPrice(cartItem.getTotalPrice() + ( quantity * product.getCostPrice()));
        cartItemRepository.save(cartItem);
      }
    }
    cart.setCartItem(cartItems);
    int totalItems = totalItems(cart.getCartItem());
    double totalPrice = totalPrice(cart.getCartItem());

    cart.setTotalPrices(totalPrice);
    cart.setTotalItems(totalItems);
    if (cart.getUser() == null) {
      cart.setUser(user);
    }

    return cartRepository.save(cart);
  }

  @Override
  public Cart updateItemInCart(Product product, int quantity, User user, Long cartId) {
    if (cartId == null && user == null) {
      return null;
    }
    Cart cart;
    if (cartId != null) {
      cart = cartRepository.findById(cartId).get();
    } else {
      cart = user.getCart();
    }

    Set<CartItem> cartItems = cart.getCartItem();

    CartItem item = findCartItem(cartItems, product.getId());

    item.setQuantity(quantity);
    item.setTotalPrice(quantity * product.getCostPrice());

    cartItemRepository.save(item);

    int totalItems = totalItems(cartItems);
    double totalPrice = totalPrice(cartItems);

    cart.setTotalItems(totalItems);
    cart.setTotalPrices(totalPrice);

    return cartRepository.save(cart);
  }

  @Override
  public Cart deleteItemFromCart(Product product, User user, Long cartId) {
    if (user == null && cartId == null) {
      return null;
    }
    Cart cart;
    if (cartId != null) {
      cart = cartRepository.findById(cartId).get();
    } else {
      cart = user.getCart();
    }
    Set<CartItem> cartItems = cart.getCartItem();
    CartItem item = findCartItem(cartItems, product.getId());
    if (item != null) {
      cartItems.remove(item);
      cartItemRepository.delete(item);

      double totalPrice = totalPrice(cartItems);
      int totalItems = totalItems(cartItems);

      cart.setCartItem(cartItems);
      cart.setTotalItems(totalItems);
      cart.setTotalPrices(totalPrice);
    }
    return cartRepository.save(cart);
  }

  private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
    if (cartItems == null) {
      return null;
    }
    CartItem cartItem = null;
    for (CartItem item : cartItems) {
      if (item.getProduct().getId() == productId) {
        cartItem = item;
      }
    }
    return cartItem;
  }

  private int totalItems(Set<CartItem> cartItems) {
    int totalItems = 0;
    for (CartItem item : cartItems) {
      totalItems += item.getQuantity();
    }
    return totalItems;
  }

  private double totalPrice(Set<CartItem> cartItems) {
    double totalPrice = 0.0;
    for (CartItem item : cartItems) {
      totalPrice += item.getTotalPrice();
    }
    return totalPrice;
  }
}
