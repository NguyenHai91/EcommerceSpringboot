package com.selfcode.ecommerce2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "wish_item")
public class WishItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean actived = true;

  @ManyToOne
  @JoinColumn(name = "wishlist_id")
  private Wishlist wishlist;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

}
