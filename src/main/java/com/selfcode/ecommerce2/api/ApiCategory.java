package com.selfcode.ecommerce2.api;

import com.selfcode.ecommerce2.dto.CategoryDto;
import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiCategory {

  @Autowired
  private CategoryService categoryService;

  @RequestMapping("/categories")
  public ResponseEntity<List<Category>> getMainCategories() {
    List<Category> categoriesDto = categoryService.getMainCategories();
    return ResponseEntity.ok(categoriesDto);
  }
}
