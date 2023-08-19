package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.dto.CategoryDto;
import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.repository.CategoryRepository;
import com.selfcode.ecommerce2.repository.ProductRepository;
import com.selfcode.ecommerce2.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ProductRepository productRepository;

  @Override
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  @Override
  public Category save(Category category) {
    try {
      return categoryRepository.save(category);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Category getById(Long id) {
    return categoryRepository.getById(id);
  }

  @Override
  public Category update(Category category) {
    Category editCategory = categoryRepository.getById(category.getId());
    editCategory.setName(category.getName());
    editCategory.setParentCategory(category.getParentCategory());
    return categoryRepository.save(editCategory);
  }

  @Override
  public void deleteById(Long id) {
    Category category = categoryRepository.getById(id);
    category.set_deleted(true);
    category.set_actived(false);
    categoryRepository.save(category);
  }

  @Override
  public void enableById(Long id) {
    Category category = categoryRepository.getById(id);
    category.set_deleted(false);
    category.set_actived(true);
    categoryRepository.save(category);
  }

  @Override
  public List<Category> findAllByActivated() {
    return categoryRepository.findAllByActivated();
  }

  @Override
  public List<CategoryDto> getCategoryAndProduct() {
    List<CategoryDto> categories = categoryRepository.getCategoryAndProduct();
    return categories;
  }

  @Override
  public List<Category> getMainCategories() {
    List<Category> categories = categoryRepository.findAll();
    List<Category> mainCategories = new ArrayList<>();
    for (Category category : categories) {
      if (category.getParentCategory() == null) {
        mainCategories.add(category);
      }
    }
    return mainCategories;
  }
}
