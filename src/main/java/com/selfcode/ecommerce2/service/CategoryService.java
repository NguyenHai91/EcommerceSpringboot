package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.dto.CategoryDto;
import com.selfcode.ecommerce2.model.Category;

import java.util.List;

public interface CategoryService {
  List<Category> findAll();
  Category save(Category category);
  Category getById(Long id);
  Category update(Category category);
  void deleteById(Long id);
  void enableById(Long id);
  List<Category> findAllByActivated();
  List<CategoryDto> getCategoryAndProduct();
  List<Category> getMainCategories();
}
