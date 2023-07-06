package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.Cart;
import com.selfcode.ecommerce2.model.CartItem;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.repository.CartItemRepository;
import com.selfcode.ecommerce2.repository.CartRepository;
import com.selfcode.ecommerce2.repository.ProductRepository;
import com.selfcode.ecommerce2.service.CartService;
import com.selfcode.ecommerce2.service.ProductService;
import com.selfcode.ecommerce2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class CartServiceImpl implements CartService {
  @Autowired
  UserService userService;

  @Autowired
  CartRepository cartRepository;

  @Autowired
  CartItemRepository cartItemRepository;

  @Autowired
  ProductService productService;

  @Override
  public Cart save(Cart cart) {
    return cartRepository.save(cart);
  }

  @Override
  public Optional<Cart> getCartById(Long id) {
    return cartRepository.findById(id);
  }

  @Override
  public Cart getCartExisting(Principal principal, HttpSession session) {
    Cart cart = null;

    if (principal != null) {
      User customer = userService.findByUsername(principal.getName());
      cart = customer.getCart();
      return cart;
    }

    if (session.getAttribute("cart") != null) {
      Long idCart = (Long) session.getAttribute("cart");
      if (cartRepository.findById(idCart).isPresent()) {
        cart = cartRepository.findById(idCart).get();
      }
      return cart;
    }
    return cart;
  }

  @Override
  public Cart addItemToCart(Product product, int quantity, User user, Long cartId) {
    Cart cart = null;
    if (cartId != null) {
      if (cartRepository.findById(cartId).isPresent()) {
        cart = cartRepository.findById(cartId).get();
      }
    } else {
      if(user != null) {
        cart = user.getCart();
      }
    }

    if (cart == null) {
      cart = new Cart();
      cart = cartRepository.save(cart);
    }
    Set<CartItem> cartItems = cart.getCartItems();
    CartItem cartItem = findCartItem(cartItems, product.getId());
    if (cartItems == null) {
      cartItems = new HashSet<>();
      if (cartItem == null) {
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setTotalPrice(quantity * product.getSalePrice());
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);
        cartItem = cartItemRepository.save(cartItem);
        cartItems.add(cartItem);
      }
    } else {
      if (cartItem == null) {
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setTotalPrice(quantity * product.getSalePrice());
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);
        cartItem = cartItemRepository.save(cartItem);
        cartItems.add(cartItem);
      } else {
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setTotalPrice(cartItem.getTotalPrice() + ( quantity * product.getSalePrice()));
        cartItemRepository.save(cartItem);
      }
    }
    cart.setCartItems(cartItems);
    int totalItems = totalItems(cart.getCartItems());
    double totalPrice = totalPrice(cart.getCartItems());

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

    Set<CartItem> cartItems = cart.getCartItems();

    CartItem item = findCartItem(cartItems, product.getId());

    item.setQuantity(quantity);
    item.setTotalPrice(quantity * product.getSalePrice());

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
    Set<CartItem> cartItems = cart.getCartItems();
    CartItem item = this.findCartItem(cartItems, product.getId());
    if (item != null) {
      cartItems.remove(item);
      cartItemRepository.delete(item);

      double totalPrice = totalPrice(cartItems);
      int totalItems = totalItems(cartItems);

      cart.setCartItems(cartItems);
      cart.setTotalItems(totalItems);
      cart.setTotalPrices(totalPrice);
    }
    return cartRepository.save(cart);
  }

  @Override
  public void clearCart(Long idCart, User customer) {
    if (!cartRepository.findById(idCart).isPresent()) {
      return;
    }

    Cart cart = cartRepository.findById(idCart).get();
    if (cart.getCartItems().size() > 0) {
      Set<CartItem> listCartItems = cart.getCartItems();
      for (CartItem item: listCartItems) {
        item = cartItemRepository.getById(item.getId());
        Optional<Product> productData = productService.getProductById(item.getProduct().getId());
        if (productData.isPresent()) {
          Product product = productData.get();
          productService.updateQuantity(product, - item.getQuantity());
        }
        this.deleteItemFromCart(item.getProduct(), customer, cart.getId());
      }
    }
    cartRepository.delete(cart);
  }

  @Override
  public void deleteCart(Cart cart) {
    cartRepository.delete(cart);
  }

  private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
    if (cartItems == null) {
      return null;
    }
    CartItem cartItem = null;
    for (CartItem item : cartItems) {
      if (item.getProduct().getId().equals(productId)) {
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
