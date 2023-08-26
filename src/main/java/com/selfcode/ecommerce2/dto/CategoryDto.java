package com.selfcode.ecommerce2.dto;


import com.selfcode.ecommerce2.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDto {
  private Long id;
  private String name;
  private Category parentCategory;
  private Long numberOfProduct;

  public CategoryDto (Category category) {
    this.id = category.getId();
    this.name = category.getName();
    this.parentCategory = category.getParentCategory();
  }
}
