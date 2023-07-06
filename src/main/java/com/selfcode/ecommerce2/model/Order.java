package com.selfcode.ecommerce2.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_date")
  private Date orderDate;

  @Column(name = "delivery_date")
  private Date deliveryDate;

  @Column(name = "total_price")
  private double totalPrice;

  @Column(name = "shipping_fee")
  private double shippingFee;

  @Column(name = "order_status")
  private String orderStatus;

  private String notes;

  @ManyToOne()
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToMany(mappedBy = "order")
  private List<OrderDetail> orderDetailList = new ArrayList<>();
}
