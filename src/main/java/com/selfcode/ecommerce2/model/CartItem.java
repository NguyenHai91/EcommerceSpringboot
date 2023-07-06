package com.selfcode.ecommerce2.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private int quantity;

  private double totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", referencedColumnName = "id")
  private Cart cart;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  private Product product;

  private boolean active = true;

  public double getTotalPrice() {
    return Math.round(totalPrice * 100)/100.00;
  }

  public double getTotalTax() {
    double totalTax = (quantity * product.getSalePrice() * product.getTax())/100;
    return Math.round(totalTax * 100)/100.00;
  }
}
