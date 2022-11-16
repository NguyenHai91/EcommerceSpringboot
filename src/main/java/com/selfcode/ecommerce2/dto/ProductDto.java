package com.selfcode.ecommerce2.dto;

import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.model.ImageProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
  private Long id;

  private String name;

  private String description;

  private Double costPrice;

  private Double salePrice;

  private int quantity;

  private int currentQuantity;

  private int views = 0;

  private Double tax = 10d;

  private Category category;

  private String image;

  private List<String> listImages;

  private boolean actived = true;

  private boolean deleted = false;
}
