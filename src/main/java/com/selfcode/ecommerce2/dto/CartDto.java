package com.selfcode.ecommerce2.dto;


import com.selfcode.ecommerce2.model.CartItem;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
public class CartDto {
  public Long id;

  public int totalItems;

  public double totalPrices;

  public User user;

  public Set<CartItem> cartItem;

  public boolean isExistingProductInCart(Product product) {
    boolean isExisting = false;
    for (CartItem item: this.cartItem) {
      if (item.getProduct().getId().equals(product.getId())) {
        isExisting = true;
        break;
      }
    }
    return isExisting;
  }
}
