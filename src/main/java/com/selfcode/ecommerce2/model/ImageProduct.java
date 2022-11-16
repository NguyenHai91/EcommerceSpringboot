package com.selfcode.ecommerce2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image_product")
public class ImageProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(columnDefinition = "MEDIUMBLOB")
  private String name;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
}
