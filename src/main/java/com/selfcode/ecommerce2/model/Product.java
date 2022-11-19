package com.selfcode.ecommerce2.model;


import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))

public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Long id;

  private String name;

  private String description;

  private double costPrice;

  private double salePrice;

  private int quantity;

  private int currentQuantity;

  private int views;

  private double tax;

  private boolean is_deleted;

  private boolean is_actived;

  private String image;

  @OneToMany(mappedBy = "product")
  private List<ImageProduct> listImages;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;


}
