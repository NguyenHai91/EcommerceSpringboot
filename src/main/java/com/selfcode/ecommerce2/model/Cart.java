package com.selfcode.ecommerce2.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "total_items")
  private int totalItems;

  @Column(name = "total_prices")
  private double totalPrices;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
  private Set<CartItem> cartItems = new HashSet<>();

  public double getSubAmount() {
    double subAmount = 0.0d;
    for (CartItem item : this.cartItems) {
      subAmount += item.getTotalPrice();
    }
    return Math.round(subAmount * 100)/100.00;
  }

  public double getTotalTax() {
    double taxAmount = 0.0d;
    for (CartItem item: this.cartItems) {
      taxAmount += item.getTotalTax();
    }
    return Math.round(taxAmount*100)/100.00;
  }

  public double getTotalAmount() {
    double totalAmount = 0.0d;
    totalAmount = this.getSubAmount() + this.getTotalTax();
    return Math.round(totalAmount*100)/100.00;
  }
}
